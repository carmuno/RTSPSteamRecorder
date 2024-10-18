package org.home.sync;

import org.home.sync.config.CameraConfig;
import org.home.sync.recording.VideoRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
public class ManagmentService implements AutoCloseable{

    /**
     * El logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ManagmentService.class);

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
    public ManagmentService(String jsonConfig) throws IOException {
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

        // Ejecutar cada tarea de cámara en un hilo separado
        for (CameraConfig cameraConfig : cameraConfigList) {
            executorService.execute(() -> processCamera(cameraConfig));
        }
    }

    /**
     * Procesa el stream de una cámara y lo guarda en disco utilizando {@link VideoRecorder}.
     *
     * @param cameraConfig Información de la cámara que se procesará.
     */
    private void processCamera(CameraConfig cameraConfig) {
        try {
            new VideoRecorder(cameraConfig).recordStream();
        } catch (IOException e) {
            logger.error("Error al procesar la cámara: " + cameraConfig.getName() + " -> " + e.getMessage());
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
