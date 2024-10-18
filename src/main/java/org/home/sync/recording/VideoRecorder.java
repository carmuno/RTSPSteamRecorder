package org.home.sync;

import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.slf4j.Logger;

/**
 * La clase {@code StreamToDiskSaver} es responsable de capturar el stream RTSP desde una cámara IP
 * y guardar ese stream en archivos segmentados en el disco.
 * Implementa la interfaz {@code Runnable} para permitir la ejecución del proceso de grabación en un hilo separado.
 * <p>
 * Esta clase utiliza FFmpeg para realizar la conversión y segmentación del stream, y guarda los fragmentos
 * en el formato MPEG-TS.
 * </p>
 * <p>
 * Los parámetros clave para la configuración del stream y del proceso de FFmpeg son proporcionados por la clase
 * {@code CameraConnectionInfo}.
 * </p>
 *
 * @author Carlos Noe Muñoz (cnoemunoz@gmail.com)
 */
public class VideoRecorder implements Runnable {

    /**
     * El logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(VideoRecorder.class);

    /**
     * Toda la información necesaria para poder hacer el Stream.
     */
    private final CameraConnectionInfo cameraConnectionInfo;

    /**
     * Comando para invocar FFmpeg.
     */
    public static final String FFMPEG_COMMAND = "ffmpeg";

    /**
     * Flag para indicar la URL de entrada.
     */
    public static final String INPUT_FLAG = "-i";

    /**
     * Flag para definir el códec de video a utilizar.
     */
    public static final String VIDEO_CODEC_FLAG = "-c:v";

    /**
     * Flag para definir el códec de audio a utilizar.
     */
    public static final String AUDIO_CODEC_FLAG = "-c:a";

    /**
     * Flag para definir el formato de salida.
     */
    public static final String FORMAT_FLAG = "-f";

    /**
     * Flag para definir el tiempo de duración de cada segmento en segundos.
     */
    public static final String SEGMENT_TIME_FLAG = "-segment_time";

    /**
     * Flag para reiniciar los timestamps en cada nuevo archivo.
     */
    public static final String RESET_TIMESTAMPS_FLAG = "-reset_timestamps";

    /**
     * Flag para definir el protocolo de transporte RTSP.
     */
    public static final String RTSP_TRANSPORT_FLAG = "-rtsp_transport";

    /**
     * Construcción de un StreamToDiskSaver a partir de un CameraConnectionInfo
     * @param cameraConnectionInfo el CameraConnectionInfo
     */
    public VideoRecorder(CameraConnectionInfo cameraConnectionInfo) {
        this.cameraConnectionInfo = cameraConnectionInfo;
    }

    /**
     * Encargado de realizar el proceso de recuperar los datos del stream, y volcarlos
     * en el fichero de salida que le hemos indicado.
     * @throws IOException
     */
    public void recordStream()
    throws IOException {
        String rtspUrl = cameraConnectionInfo.getRtspUrl();
        String outputPattern = cameraConnectionInfo.getName() + "/output_%03d.ts";  // Usamos el formato .ts

        try {
            Path p = Files.createDirectory(Path.of(cameraConnectionInfo.getName()));
            logger.info("nuevo directorio creado" + p);
        } catch (FileAlreadyExistsException e){
            logger.info("directorio ya creado de forma previa");
        }

        var process = getProcess(rtspUrl, outputPattern);

        printLogsRecordStream(process);
        try {
            int exitCode = process.waitFor(); // Espera a que el proceso termine
            logger.info("Comando ejecutado: ffmpeg -i " + rtspUrl + " -c:v libx264 -c:a aac -f segment -segment_time 300 -reset_timestamps 1 " + outputPattern);
            logger.info("exit code" + exitCode);
        } catch (InterruptedException e) {
            logger.error("Error al ejecutar el proceso de descarga de stream en fichero", e);
        }
    }

    /**
     * Me pinta los logs que obtiene de la salida del comando.
     * @param process el proceso.
     * @throws IOException Si ocurre una exception de tipo I/O.
     */
    private void printLogsRecordStream(Process process) throws IOException {
        try(BufferedReader bufferedInputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            String line;
            logger.info("Thread actual:" + Thread.currentThread().getName());
            while ((line = bufferedInputStreamReader.readLine()) !=null) {
                logger.info(line);
            }
        }
    }

    /**
     * Crea y devuelve un proceso de FFmpeg configurado para capturar y segmentar el stream RTSP especificado.
     * Este método construye el comando FFmpeg utilizando los parámetros de video y audio definidos,
     * y configura la segmentación del archivo de salida en partes de la duración especificada.
     * <p>
     * El proceso ejecuta FFmpeg para leer el stream desde la URL RTSP proporcionada y guardar
     * el contenido en un archivo de salida segmentado según el patrón proporcionado.
     * </p>
     *
     * @param rtspUrl La URL del stream RTSP que será capturado.
     * @param outputPattern El patrón de nombre para los archivos de salida segmentados (por ejemplo, "output_%03d.ts").
     * @return El proceso {@link Process} configurado para ejecutar FFmpeg.
     * @throws IOException Si ocurre un error al crear el proceso o si hay problemas de entrada/salida durante su ejecución.
     */
    private Process getProcess(String rtspUrl, String outputPattern) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                Arrays.asList(
                        FFMPEG_COMMAND,
                        INPUT_FLAG, rtspUrl,
                        VIDEO_CODEC_FLAG, cameraConnectionInfo.getVideoCodec(),
                        AUDIO_CODEC_FLAG, cameraConnectionInfo.getAudioCodec(),
                        FORMAT_FLAG, cameraConnectionInfo.getFormat(),
                        SEGMENT_TIME_FLAG, cameraConnectionInfo.getSegmentTime(),
                        RESET_TIMESTAMPS_FLAG, cameraConnectionInfo.getResetTimeStamps(),
                        RTSP_TRANSPORT_FLAG, cameraConnectionInfo.getRTSPTransport(),
                        outputPattern
                )
        );

        processBuilder.redirectErrorStream(true); // Redirige errores y salida estándar juntos
        return processBuilder.start();
    }

    @Override
    public void run() {
        try {
            recordStream();
        } catch (IOException e) {
            logger.error("error al ejecutar el proceso para la camara " + cameraConnectionInfo.toString(), e);
            logger.info("vamos a recuperarnos del proceso");
            logger.info("volviendo a ejecutar de nuevo mi proceso en mi hilo");
            run();
        }
    }
}

