package org.home.sync.recording;

/**
 * La enumeración {@code StreamType} define los tipos de streams admitidos por el sistema.
 * Cada tipo de stream representa una calidad de video proporcionada por la cámara IP.
 * <p>
 * Estos tipos se utilizan para seleccionar el stream adecuado según la resolución o la calidad
 * deseada al conectar y realizar streaming desde una cámara IP.
 * </p>
 *
 * @author Carlos Noe Muñoz (cnoemunoz@gmail.com)
 */
public enum StreamQuality {

    /**
     * Stream de alta resolución.
     */
    STREAM1,

    /**
     * Stream de baja resolución.
     */
    STREAM2
}
