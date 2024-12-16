package org.home.sync.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.home.sync.recording.StreamQuality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * La clase {@code CameraConnectionInfo} encapsula toda la información necesaria para establecer una conexión RTSP con una cámara IP.
 * <p>
 * Esta clase contiene detalles sobre la configuración de las cámaras, como el nombre del dispositivo, el usuario,
 * la contraseña, la dirección IP, el puerto, y el stream. Además, permite personalizar los parámetros de conversión de video y audio
 * que se utilizarán para el procesamiento de las transmisiones RTSP con herramientas como FFmpeg.
 * </p>
 * Se proporciona una función para cargar la configuración de las cámaras desde un archivo JSON con el método estático
 * {@link #fromJsonFile(String)}.</p>
 *
 * <p>Ejemplo de JSON para la configuración:</p>
 * <pre>
 * [
 *   {
 *     "name": "jardin-trasero",
 *     "user": "admin",
 *     "password": "xxxxxx",
 *     "ip": "192.168.1.50",
 *     "port": 554,
 *     "stream": "stream1"
 *   }
 * ]
 * </pre>
 *
 * @author Carlos Noé Muñoz (cnoemunoz@gmail.com)
 */
public class CameraConfig {

    /**
     * El logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(CameraConfig.class);

    /**
     * Si se quiere o no clonar la retrasmisión RTSP. Se hace para evitar la ocupación de los canales de Stream
     * {@link StreamQuality} por el funcionamiento de la libreria u otros.
     */
    private RTSPCloneConfig cloneRTSPStream;

    /**
     * El name que se le da al dispositivo.
     */
    private final String name;

    /**
     * URL final para aplicar el protocolo.
     */
    private String rtspUrl;

    /**
     * User del protocolo rtps
     */
    private String user;

    /**
     * password del protocolo rtps
     */
    private String password;

    /**
     * ip-de-la-camara a la que vamos a estableces el flujo.
     */
    private final String ip;

    /**
     * Puerto de la camara a la que vamos a establecer el flujo.
     */
    private final int port;

    /**
     * URL adicional de acceso.
     */
    private final StreamQuality stream;

    /**
     * Códec de video utilizado para la conversión.
     */
    public String videoCodec = "libx264";

    /**
     * Códec de audio utilizado para la conversión.
     */
    public String audioCodec = "aac";

    /**
     * Formato de salida que segmenta el archivo en múltiples partes.
     */
    public String format = "segment";

    /**
     * Duración de cada segmento en segundos.
     */
    public String segmentTime = "300";

    /**
     * Valor para reiniciar los timestamps en cada nuevo archivo.
     */
    public String resetTimeStamps = "1";

    /**
     * Protocolo de transporte utilizado (TCP) para la transmisión RTSP.
     */
    public String RTSPTransport = "tcp";

    /**
     * Contructor para generar la rtspUrl.
     *
     * @param name     el name.
     * @param user     el user.
     * @param password el pass.
     */
     public CameraConfig(String name, String user, String password, String ip, int port, StreamQuality stream, RTSPCloneConfig cloneRTSPStream)
     throws UnknownHostException {
         if (name == null || name.trim().isEmpty()) {
             throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
         }
         if (user == null || user.trim().isEmpty()) {
             throw new IllegalArgumentException("El usuario no puede ser nulo o vacío.");
         }
         if (password == null || password.trim().isEmpty()) {
             throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
         }
         if (ip == null || ip.trim().isEmpty()) {
             throw new IllegalArgumentException("La IP no puede ser nula o vacía.");
         }
         if (port <= 0) {
             throw new IllegalArgumentException("El puerto debe ser un número positivo.");
         }
         if (stream == null) {
             throw new IllegalArgumentException("El stream no puede ser nulo. Especifica stream1 o stream2");
         }
         this.name = name;
         this.user = user;
         this.password = password;
         this.ip = ip;
         this.port = port;
         this.stream = stream;
         this.rtspUrl = generatertspUrlWithOutPath();
         this.cloneRTSPStream = cloneRTSPStream;
     }

    public String generatertspUrlWithOutPath() {
        return "rtsp://" + this.user + ":" + this.password + "@" + this.ip + ":" + this.port;
    }

    /**
     * Lee un archivo JSON con la configuración de las cámaras y genera una lista de objetos {@code CameraConnectionInfo}.
     * El archivo JSON debe contener la información necesaria para establecer las conexiones a las cámaras, incluyendo
     * usuario, contraseña, IP, puerto, y stream.
     * <p>
     * Después de cargar los datos, este método verifica si las cámaras en el archivo coinciden con las cámaras encontradas en la red.
     * Si no coinciden, lanza una excepción personalizada {@code CameraNotFoundException}.
     * </p>
     *
     * @param propertiesFileName Nombre del archivo JSON que contiene la configuración de las cámaras.
     * @return Una lista de objetos {@code CameraConnectionInfo} que representan las cámaras leídas desde el archivo JSON.
     * @throws IOException Si hay un problema al leer el archivo JSON.
     */
    public static List<CameraConfig> fromJsonFile(String propertiesFileName)
    throws IOException {
        try (InputStream inputStream = CameraConfig.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                throw new IOException("Archivo json '" + propertiesFileName + "' no encontrado en resources");
            }
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(CameraConfig.class, new CameraConfigDeserializer())
                    .create();

            Type type = new TypeToken<List<CameraConfig>>(){}.getType();

            return gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), type);
        }
    }

    public RTSPCloneConfig getCloneRTSPStream() {
        return cloneRTSPStream;
    }

    private String getIp() {
        return ip;
    }

    public String getRtspUrl() {
        return rtspUrl;
    }

    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public String getFormat() {
        return format;
    }

    public String getSegmentTime() {
        return segmentTime;
    }

    public String getResetTimeStamps() {
        return resetTimeStamps;
    }

    public String getRTSPTransport() {
        return RTSPTransport;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setSegmentTime(String segmentTime) {
        this.segmentTime = segmentTime;
    }

    public void setResetTimeStamps(String resetTimeStamps) {
        this.resetTimeStamps = resetTimeStamps;
    }

    public void setRTSPTransport(String RTSPTransport) {
        this.RTSPTransport = RTSPTransport;
    }

    public StreamQuality getStream() {
        return stream;
    }

}
