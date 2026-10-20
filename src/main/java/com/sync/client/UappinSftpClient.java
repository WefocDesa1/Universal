package com.sync.client;

import com.jcraft.jsch.*;
import com.sync.config.UappinSftpProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Cliente SFTP para levantar los reportes de Uappin.
 *
 * Autenticación por clave privada, con password como alternativa (útil
 * para levantar un SFTP de prueba solo con variables de entorno, sin tener
 * que montar un authorized_keys dentro del contenedor). Si ambas están
 * seteadas, gana la clave.
 *
 * El chequeo estricto de host key está deshabilitado a propósito: pensado
 * para conectar por la red privada de Railway entre dos servicios del mismo
 * proyecto, no por internet público, así que el riesgo de MITM es marginal
 * y no vale la complejidad extra de mantener un known_hosts persistente.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UappinSftpClient {

    private final UappinSftpProperties properties;

    public ChannelSftp connect() throws JSchException {
        JSch jsch = new JSch();
        Session session;

        if (properties.getPrivateKey() != null && !properties.getPrivateKey().isBlank()) {
            jsch.addIdentity("uappin-sftp", properties.getPrivateKey().getBytes(StandardCharsets.UTF_8), null, null);
            session = jsch.getSession(properties.getUsername(), properties.getHost(), properties.getPort());
        } else {
            session = jsch.getSession(properties.getUsername(), properties.getHost(), properties.getPort());
            session.setPassword(properties.getPassword());
        }

        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(properties.getConnectTimeout());
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        return channel;
    }

    public void disconnect(ChannelSftp channel) {
        if (channel == null) {
            return;
        }
        Session session = null;
        try {
            session = channel.getSession();
        } catch (JSchException e) {
            log.warn("No se pudo obtener la sesión SFTP para cerrarla: {}", e.getMessage());
        }
        channel.disconnect();
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * Lista los archivos de Excel del inbox, ordenados por nombre.
     *
     * Acepta .xlsx y .xls: el sistema de Uappin manda reportes que son xlsx
     * reales (OOXML) pero con extensión .xls, y filtrando solo por .xlsx se
     * ignoraban en silencio. El parser detecta el formato real por contenido.
     *
     * El orden importa: los archivos pueden repetir servicios (fotos móviles
     * de los últimos días) y gana el último que se procesa, así que el más
     * viejo tiene que ir primero. Los nombres llevan la fecha/hora de envío,
     * por lo que el orden alfabético coincide con el cronológico.
     *
     * Se ignoran los archivos modificados hace menos de minFileAgeSeconds: como el
     * scheduler revisa el SFTP cada pocos minutos, podría tomar uno que Uappin
     * todavía está subiendo, y un archivo leído a medias falla y queda en "error".
     * Se levanta en la próxima revisión, cuando ya terminó de subirse.
     */
    @SuppressWarnings("unchecked")
    public List<String> listXlsxFiles(ChannelSftp channel) throws SftpException {
        Vector<ChannelSftp.LsEntry> entries = channel.ls(properties.getInboxPath());
        List<String> files = new ArrayList<>();
        long ahora = System.currentTimeMillis() / 1000;
        for (ChannelSftp.LsEntry entry : entries) {
            String name = entry.getFilename();
            String lower = name.toLowerCase();
            if (!entry.getAttrs().isDir() && (lower.endsWith(".xlsx") || lower.endsWith(".xls"))) {
                long edad = ahora - entry.getAttrs().getMTime();
                if (edad < properties.getMinFileAgeSeconds()) {
                    log.info("[UAPPIN] {} se deja para la próxima revisión: modificado hace {}s (mínimo {}s), puede estar subiéndose",
                            name, edad, properties.getMinFileAgeSeconds());
                    continue;
                }
                files.add(name);
            }
        }
        Collections.sort(files);
        return files;
    }

    public byte[] download(ChannelSftp channel, String filename) throws SftpException, IOException {
        String remotePath = properties.getInboxPath() + "/" + filename;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            channel.get(remotePath, out);
            return out.toByteArray();
        }
    }

    public void moveToProcesados(ChannelSftp channel, String filename) throws SftpException {
        move(channel, filename, properties.getProcesadosPath());
    }

    public void moveToError(ChannelSftp channel, String filename) throws SftpException {
        move(channel, filename, properties.getErrorPath());
    }

    private void move(ChannelSftp channel, String filename, String destDir) throws SftpException {
        String from = properties.getInboxPath() + "/" + filename;
        String to = destDir + "/" + filename;
        channel.rename(from, to);
    }
}
