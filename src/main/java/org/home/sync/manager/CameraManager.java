package org.home.sync.manager;

import org.home.sync.config.CameraConfig;
import org.home.sync.recording.VideoRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * La clase {@code ManagmentService} es responsable de gestionar y ejecutar
 * tareas relacionadas con el procesamiento de cámaras IP. En este caso, la de
 * grabar contenido por el flujo streamX.
 * <p>
 * Este servicio utiliza un {@link ExecutorService} con un tamaño de pool de hilos
 * basado en el número de cámaras configuradas, permitiendo que cada cámara se
 * procese de manera concurrente.
 * </p>
 *
 * <p>
 * La clase implementa {@link AutoCloseable}, lo que garantiza que el servicio se cierre automáticamente
 * cuando se use dentro de un bloque try-with-resources.
 * </p>
 * @author Carlos Noé Muñoz (cnoemunoz@gmail.com)
 */
public class CameraManager implements AutoCloseable{

    /**
     * El logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(CameraManager.class);

    /**
     * Executor service que gestiona un pool de hilos para ejecutar tareas concurrentemente.
     * Cada cámara se procesa en un hilo separado. (abre un puente rtsp distinto).
     */
    private ExecutorService executorService;

    /**
     * Constructor que inicializa el servicio de gestión de cámaras.
     * Carga la configuración de las cámaras desde un archivo JSON y asigna el número de hilos en función
     * de la cantidad de cámaras detectadas.
     *
     * @param jsonConfig Ruta del archivo de configuración JSON que contiene la información de las cámaras.
     * @throws IOException Si ocurre un error al cargar la configuración de las cámaras.
     */
    public CameraManager(String jsonConfig) throws IOException {
        executeService(jsonConfig);
    }

    /**
     * Inicializa el servicio cargando las cámaras desde el archivo JSON y configurando
     * el pool de hilos para el procesamiento concurrente.
     *
     * @param jsonConfig Ruta del archivo de configuración JSON.
     * @throws IOException Si ocurre un error al cargar la configuración de las cámaras.
     */
    private void executeService(String jsonConfig) throws IOException {
        // Cargar la lista de cámaras desde el archivo JSON
        List<CameraConfig> cameraConfigList = CameraConfig.fromJsonFile(jsonConfig);

        // Establecer el tamaño del pool de hilos basado en el número de cámaras
        int sizePool = cameraConfigList.size();
        this.executorService = Executors.newFixedThreadPool(sizePool);

        if(cameraConfigList.stream().anyMatch(a -> {
            return a.getCloneRTSPStream() != null;
        })) {
            executorService.execute(CameraManager::startMediamtx); //si hay alguna entrada para duplicar el stream, levantamos el servidor de duplicación mediamtx.
        }
        // Ejecutar cada tarea de cámara en un hilo separado
        for (CameraConfig cameraConfig : cameraConfigList) {
            executorService.execute(new VideoRecorder(cameraConfig));
        }
    }

    private static void startMediamtx() {
        try {
            ProcessBuilder pb = new ProcessBuilder("./install-mediamtx.sh");
            pb.inheritIO();
            Process process = pb.start();
            try(BufferedReader bufferedInputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                String line;
                logger.info("Thread actual:" + Thread.currentThread().getName());
                while ((line = bufferedInputStreamReader.readLine()) !=null) {
                    logger.info(line);
                }
            }
            logger.info("mediamtx iniciado correctamente.");
        } catch (IOException e) {
            logger.error("Error al iniciar mediamtx.");
        }
    }

    /**
     * Cierra el servicio de ejecución. Es importante llamar a este método una vez
     * que todas las tareas se hayan completado para liberar los recursos del {@link ExecutorService}.
     */
    @Override
    public void close() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            logger.info("Servicio de gestión de cámaras cerrado correctamente.");
        }
    }
}
