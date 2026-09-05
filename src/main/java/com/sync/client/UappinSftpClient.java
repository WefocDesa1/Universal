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
import java.util.List;
import java.util.Vector;

/**
 * Cliente SFTP para levantar los reportes de Uappin.
 *
 * Autenticación por clave privada (sin password). El chequeo estricto de
 * host key está deshabilitado a propósito: la conexión va por la red
 * privada de Railway entre dos servicios del mismo proyecto, no por
 * internet público, así que el riesgo de MITM es marginal y no vale la
 * complejidad extra de mantener un known_hosts persistente.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UappinSftpClient {

    private final UappinSftpProperties properties;

    public ChannelSftp connect() throws JSchException {
        JSch jsch = new JSch();
        jsch.addIdentity("uappin-sftp", properties.getPrivateKey().getBytes(StandardCharsets.UTF_8), null, null);

        Session session = jsch.getSession(properties.getUsername(), properties.getHost(), properties.getPort());
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

    @SuppressWarnings("unchecked")
    public List<String> listXlsxFiles(ChannelSftp channel) throws SftpException {
        Vector<ChannelSftp.LsEntry> entries = channel.ls(properties.getInboxPath());
        List<String> files = new ArrayList<>();
        for (ChannelSftp.LsEntry entry : entries) {
            String name = entry.getFilename();
            if (!entry.getAttrs().isDir() && name.toLowerCase().endsWith(".xlsx")) {
                files.add(name);
            }
        }
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
