package org.home.sync.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuración para la duplicación de transmisiones RTSP.
 *
 * @author Carlos Noé Muñoz (cnoemunoz@gmail.com).
 */
public class RTSPCloneConfig {

    /**
     * El formato de salida. como es una duplicación, sera RTSP.
     */
    private String format = "rtsp";

    /**
     * Lista de endpoints de salida para la duplicación de la transmisión RTSP.
     * puede ser uno o n.
     */
    private List<String> outputEndpoints;

    /**
     * Protocolo de transporte para la clonación de transmisiones (por ejemplo, TCP o UDP).
     */
    private String cloneTransport = "tcp";

    /**
     * Servidor RTSP donde replicar la señal
     */
    private String rtspSever = "127.0.0.1";

    /**
     * Puerto del servidor RTSP donde se va a replicar la señal.
     */
    private int port = 8554;

    /**
     * Obtiene el server rtsp donde se va a replicar la señal.
     * (el esqueleto)
     * @return El server rtsp donde se va a replicar la señal.
     */
    private String getRtspServer() {
        return "rtsp://" + rtspSever + ":" + port + "/";
    }

    /**
     * Obtiene los servidores rtsp espejo a clonar la señar
     * @return
     */
    public List<String> getRtspServers() {
        List<String> list = new ArrayList<>();
        for(String outpoint : getOutputEndpoints()){
            list.add(getRtspServer() + outpoint);
        }
        return list;
    }

    public String getFormat() {
        return format;
    }

    public List<String> getOutputEndpoints() {
        return outputEndpoints;
    }

    public void setOutputEndpoints(List<String> outputEndpoints) {
        this.outputEndpoints = outputEndpoints;
    }

    public String getCloneTransport() {
        return cloneTransport;
    }

    @Override
    public String toString() {
        return "RTSPCloneConfig{" +
                ", outputEndpoints=" + outputEndpoints +
                '}';
    }

    public String getRtspSever() {
        return rtspSever;
    }

    public int getPort() {
        return port;
    }

    public void setRtspSever(String rtspSever) {
        this.rtspSever = rtspSever;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
