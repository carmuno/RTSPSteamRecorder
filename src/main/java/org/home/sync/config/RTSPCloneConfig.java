package org.home.sync.config;

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
    private final String format = "rtsp";

    /**
     * Lista de endpoints de salida para la duplicación de la transmisión RTSP.
     * puede ser uno o n.
     */
    private List<String> outputEndpoints;

    /**
     * Protocolo de transporte para la clonación de transmisiones (por ejemplo, TCP o UDP).
     */
    private String cloneTransport = "tcp";


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
}
