# Integración Magiis — Viajes Histórico y Cuentas Corrientes

Documenta los dos endpoints de Magiis desarrollados en el branch `magiis`, su configuración y cómo probarlos. Verificado contra la API real de Magiis el 2026-08-19.

## 1. Qué está implementado

| Endpoint Magiis | Método | Uso en el código | Endpoint local para disparar el sync |
|---|---|---|---|
| `/trip/query/listHistory` | POST | `MagiisSyncService.syncViajes()` | `POST /api/sync/magiis/viajes?desde=&hasta=` |
| `/client/checking_account_balance/query/list` | POST | `MagiisSyncService.syncCuentasCorrientes()` | `POST /api/sync/magiis/cuentas-corrientes` |

Ambos quedan persistidos en Postgres (`magiis_viajes` y `magiis_cuentas_corrientes`) y se puede volver a correr el sync las veces que haga falta: hace upsert (crea si no existe, actualiza si ya existe) por una clave de negocio propia de cada entidad, no por PK autogenerada.

Hay un tercer endpoint ya cableado en el cliente pero **sin usar** por ningún sync: `/trip/query/list` (viajes "recientes", no histórico) — queda disponible en `MagiisClient.getViajes()` por si se necesita a futuro.

## 2. Arquitectura / flujo de código

```
SyncController (REST)
   └─> MagiisSyncService            (paginación, upsert, logging)
          ├─> MagiisTokenService     (login + cache de token 50 min)
          │      └─> MagiisClient#login
          └─> MagiisClient           (Feign, un método por endpoint)
                 ├─> getViajesHistorico()
                 └─> getCuentasCorrientes()
```

- **`MagiisClient`** ([src/main/java/com/sync/client/MagiisClient.java](../src/main/java/com/sync/client/MagiisClient.java)): interfaz Feign, `base-url` viene de `magiis.api.base-url`. Todos los endpoints de Magiis son POST, incluso los de consulta.
- **`MagiisTokenService`** ([src/main/java/com/sync/service/MagiisTokenService.java](../src/main/java/com/sync/service/MagiisTokenService.java)): hace login y cachea el token en memoria (no hay expiración documentada por la API, así que se renueva cada 50 min por las dudas). Reintenta 3 veces con backoff exponencial (`@Retryable`, delay 2s, multiplicador 2).
- **`MagiisSyncService`** ([src/main/java/com/sync/service/MagiisSyncService.java](../src/main/java/com/sync/service/MagiisSyncService.java)): recorre todas las páginas de la respuesta (`while page <= totalPages`), mapea DTO → Entity, guarda `raw_json` completo por si hace falta reprocesar campos que no se persistieron en columnas propias, y registra cada corrida en `sync_execution_log` (tabla compartida con los syncs de Xubio).
- **`MagiisScheduler`** ([src/main/java/com/sync/scheduler/MagiisScheduler.java](../src/main/java/com/sync/scheduler/MagiisScheduler.java)): corre los mismos syncs por cron, deshabilitado por default (ver sección Configuración).
- **`SyncController`** ([src/main/java/com/sync/controller/SyncController.java](../src/main/java/com/sync/controller/SyncController.java)): expone ambos syncs como POST manuales para debugging/demos, sin necesidad de esperar al scheduler.

## 3. Autenticación contra Magiis

```
POST {base-url}/auth/login
Content-Type: application/json
Body: {"username": "...", "password": "..."}

Response 200: {"token": "Bearer eyJhbGciOi..."}
```

El token que devuelve la API **ya incluye el prefijo "Bearer "**, así que se usa tal cual en el header `Authorization` de los requests siguientes. Cada request a Magiis también necesita el header `ApplicationName` con el mismo username usado para loguearse — sin ese header, la API responde pero con datos vacíos/errores según el endpoint.

## 4. Endpoint 1 — Viajes Histórico

`POST /trip/query/listHistory`

**Request** (`MagiisViajeQueryRequest`):
```json
{ "dateFrom": "2026-07-01", "dateTo": "2026-07-31", "page": 1 }
```
`status` también es un campo válido del request pero no se usa desde el sync actual (siempre trae todos los estados).

**Response** — paginado, forma `{ "page": { "content": [...], "currentPage": N, "totalPages": N } }` (a diferencia de cuentas corrientes, viene envuelto en `"page"`).

**Campos relevantes del viaje** (`MagiisViajeDTO` → columnas de `magiis_viajes`): `travelIdForCarrier` (clave de negocio, único), `state`, datos de pasajero y chofer, vehículo (marca/modelo/dominio), `origin`/`destination`, `creationDate`, `finalPrice`/`simulatePrice`, `paymentMethod`, `serviceType`, `isProgrammed`, `delivery`.

**Disparo manual:**
```bash
curl -s -X POST "http://localhost:8080/api/sync/magiis/viajes?desde=2026-07-01&hasta=2026-07-31"
```

## 5. Endpoint 2 — Cuentas Corrientes y Saldo

`POST /client/checking_account_balance/query/list`

**Request** (`MagiisCtaCteQueryRequest`): solo `{ "page": 1 }`, no admite rango de fechas ni filtros — trae todas las cuentas corrientes existentes, paginadas de a 100.

**Response** — paginado en la **raíz** del JSON (sin envolver en `"page"`, a diferencia de viajes):
```json
{
  "currencyCode": "$",
  "content": [ { "checkingAccountId": 28470, "name": "...", "passengerClass": "COMPANY", "balance": 0, "limit": 0 } ],
  "currentElementsQty": 100,
  "totalElements": 536,
  "currentPage": 1,
  "totalPages": 6
}
```

**Campos relevantes** (`MagiisCtaCteDTO` → columnas de `magiis_cuentas_corrientes`): `checkingAccountId` (clave de negocio, único), `name`, `passengerClass` (`COMPANY` | `INDIVIDUAL`), `balance`, `limit` → columna `limit_amount`.

**Disparo manual:**
```bash
curl -s -X POST "http://localhost:8080/api/sync/magiis/cuentas-corrientes"
```

**Verificado en producción (API real):** 536 cuentas corrientes en 6 páginas, 100% `created` en la primera corrida contra una base vacía.

## 6. Configuración (`application.yml`)

```yaml
magiis:
  api:
    base-url: ${MAGIIS_BASE_URL:https://api.public.magiis.com}
    username: ${MAGIIS_USERNAME:apitestuniversal}
    password: ${MAGIIS_PASSWORD:r6ocrghhoumjj4ceuofskk}
    connect-timeout: 5000
    read-timeout: 15000

scheduler:
  magiis:
    viajes:
      enabled: false
      cron: "0 15 * * * *"        # cada hora a los :15 (histórico)
    cuentasCorrientes:
      enabled: false
      cron: "0 0 3 * * *"         # todos los días a las 3am
```

- `base-url`, `username` y `password` son overrideables por variable de entorno (`MAGIIS_BASE_URL`, `MAGIIS_USERNAME`, `MAGIIS_PASSWORD`), pero **los defaults ya son las credenciales reales de test** (`apitestuniversal` / la de testing que usamos para probar) — quedaron hardcodeadas en el yml en algún momento de esta sesión de trabajo. Andan (las probamos contra la API real), pero es texto plano commiteado al repo, igual que la contraseña de Railway en el bloque `datasource` de este mismo archivo. Vale la pena decidir conscientemente si se dejan así o se sacan del yml antes de que el repo se comparta más ampliamente.
- Los dos schedulers de Magiis están **deshabilitados por default** (`enabled: false`). Para que corran solos hay que poner `true` o setear `SCHEDULER_MAGIIS_VIAJES_ENABLED=true` / `SCHEDULER_MAGIIS_CUENTASCORRIENTES_ENABLED=true`. Mientras tanto, la única forma de sincronizar es disparando los endpoints manuales de la sección 4/5.
- El datasource por default apunta al Postgres local de `docker-compose.yml` (`localhost:5432/syncdb`) — ver sección 7 para el detalle completo.

## 7. Configuración de conexión a base de datos

El bloque `spring.datasource` de `application.yml` quedó con dos configuraciones, una activa (local) y una comentada (Railway):

```yaml
spring:
  datasource:
    # Railway (producción) - descomentar estas 3 lineas y comentar el bloque "local" de abajo para apuntar ahi
    # url: jdbc:postgresql://trolley.proxy.rlwy.net:11598/railway
    # username: postgres
    # password: cMsOSZfQiefwWWfksSiaqcjihcJFmpnb

    # Local (docker-compose) - valores por defecto, overrideables por env var
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/syncdb}
    username: ${SPRING_DATASOURCE_USERNAME:syncuser}
    password: ${SPRING_DATASOURCE_PASSWORD:syncpassword}
    driver-class-name: org.postgresql.Driver
```

- **Por default (sin pasar nada) conecta al contenedor local**, no a Railway. Para volver a apuntar a Railway hay que descomentar sus 3 líneas y comentar el bloque "Local".
- También es overrideable por variable de entorno sin tocar el archivo: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.
- La contraseña de Railway quedó comentada pero sigue en texto plano en el repo — ver nota de la sección 6 sobre credenciales.

**Contenedor Postgres local** (`docker-compose.yml`):

| Parámetro | Valor |
|---|---|
| Host / contenedor | `localhost` (desde la app) / `sync_postgres` (desde otro contenedor, ej. pgAdmin) |
| Puerto | `5432` |
| Base de datos | `syncdb` |
| Usuario | `syncuser` |
| Password | `syncpassword` |

```bash
docker-compose up -d          # levanta sync_postgres + pgAdmin
docker exec -it sync_postgres psql -U syncuser -d syncdb   # conectarse por terminal
```

**pgAdmin** (incluido en el mismo `docker-compose.yml`, para inspeccionar la base con GUI):

- URL: `http://localhost:5050`
- Login pgAdmin: `admin@admin.com` / `admin`
- Servidor a registrar dentro de pgAdmin (`Add New Server`): Host `sync_postgres`, puerto `5432`, maintenance database `syncdb`, usuario `syncuser`, password `syncpassword`.

## 8. Cómo correrlo local

```bash
docker-compose up -d                      # levanta sync_postgres + pgAdmin (localhost:5050)
mvn spring-boot:run                       # o: java -jar target/xubio-magiis-sync-1.0.0.jar
```

Sin pasar ninguna variable de entorno ya conecta al Postgres local y usa las credenciales de test de Magiis del yml. Liquibase crea el schema completo (34 changesets, incluye `magiis_viajes` y `magiis_cuentas_corrientes`) la primera vez que corre contra una base vacía.
