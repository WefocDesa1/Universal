# xubio-magiis-sync

Servicio Spring Boot que sincroniza datos de **Xubio** y **Magiis** hacia una base de datos **PostgreSQL** local, ejecutándose como servicio con schedulers configurables.

---

## Stack

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.2 |
| HTTP Client | OpenFeign |
| ORM | Spring Data JPA / Hibernate |
| Base de datos | PostgreSQL 15 |
| Migraciones DB | Liquibase |
| Contenedores | Docker / Docker Compose |
| Retry | Spring Retry |
| Admin DB | pgAdmin 4 |

---

## Prerequisitos

- Java 17+
- Maven 3.8+
- Docker + Docker Compose

---

## 1. Levantar la base de datos con Docker

```bash
docker-compose up -d
```

Esto inicia:
- **PostgreSQL** en `localhost:5432`
- **pgAdmin** en `http://localhost:5050`

### Acceder a pgAdmin
- URL: http://localhost:5050
- Usuario: `admin@admin.com`
- Password: `admin`

### Conectar pgAdmin a la DB
1. Click en "Add New Server"
2. Name: `syncdb`
3. Connection → Host: `sync_postgres`
4. Port: `5432`
5. Database: `syncdb`
6. Username: `syncuser`
7. Password: `syncpassword`

---

## 2. Configurar credenciales de APIs

Editá `src/main/resources/application.yml`, sección `xubio.api` y `magiis.api`:

```yaml
xubio:
  api:
    client-id: TU_CLIENT_ID_XUBIO
    client-secret: TU_CLIENT_SECRET_XUBIO
```

O podés usar variables de entorno:

```bash
export XUBIO_CLIENT_ID=tu_client_id
export XUBIO_CLIENT_SECRET=tu_client_secret
```

---

## 3. Compilar y ejecutar

```bash
# Compilar
mvn clean package -DskipTests

# Ejecutar
java -jar target/xubio-magiis-sync-1.0.0.jar
```

O en modo desarrollo:

```bash
mvn spring-boot:run
```

---

## Schedulers (configurables en application.yml)

| Tarea | Cron por defecto | Descripción |
|---|---|---|
| Xubio Facturas | `0 0 * * * *` | Cada hora |
| Xubio Clientes | `0 0 2 * * *` | Todos los días 2am |
| Xubio Productos | `0 30 1 * * *` | Todos los días 1:30am |

Para deshabilitar una tarea:
```yaml
scheduler:
  xubio:
    facturas:
      enabled: false
```

---

## API REST (sync manual)

```bash
# Forzar sync de facturas Xubio (con rango de fechas opcional)
POST http://localhost:8080/api/sync/xubio/facturas?desde=2024-01-01&hasta=2024-12-31

# Forzar sync de clientes Xubio
POST http://localhost:8080/api/sync/xubio/clientes

# Forzar sync de productos Xubio
POST http://localhost:8080/api/sync/xubio/productos

```

---

## Health & Monitoring

```bash
# Estado del servicio
GET http://localhost:8080/actuator/health

# Tareas scheduladas activas
GET http://localhost:8080/actuator/scheduledtasks

# Métricas
GET http://localhost:8080/actuator/metrics
```

---

## Ajustar los DTOs a las APIs reales

Los DTOs en `src/main/java/com/sync/dto/` contienen los campos más comunes según la documentación pública disponible.
Si la API devuelve campos distintos, simplemente modificá los DTOs y las entidades correspondientes, y creá una nueva migración de Liquibase.

---

## Comandos útiles Docker

```bash
# Ver logs de la DB
docker logs sync_postgres

# Conectarse a la DB desde terminal
docker exec -it sync_postgres psql -U syncuser -d syncdb

# Detener todo
docker-compose down

# Detener y borrar datos (¡cuidado!)
docker-compose down -v
```
