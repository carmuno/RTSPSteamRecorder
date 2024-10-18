package org.home.sync;

import org.home.sync.manager.CameraManager;

import java.io.IOException;

/**
 * Clase principal de la aplicación que demuestra cómo utilizar el sistema de gestión de cámaras IP.
 * <p>
 * En este ejemplo, se carga un archivo JSON que contiene la configuración de una o más cámaras IP,
 * y se utiliza la clase {@link CameraManager} para gestionar y procesar los streams de video.
 * El archivo JSON contiene la información mínima necesaria para configurar cada cámara, como
 * nombre, usuario, contraseña, IP, puerto, y tipo de stream (alta o baja calidad).
 * Además, las configuraciones predeterminadas como códecs de video/audio y formato de segmentación
 * se aplican automáticamente.
 * </p>
 * <h3>Ejemplo de archivo JSON (`info.json`)</h3>
 * <pre>
 * [
 *   {
 *     "name": "jardin-trasero",
 *     "user": "cnoe-tapo",
 *     "password": "xxxxxx",
 *     "ip": "192.168.1.50",
 *     "port": 554,
 *     "stream": "stream2",
 *     "segmentTime": 5
 *   }
 * ]
 * </pre>
 * <p>
 * Además, se aplican las siguientes configuraciones por defecto:
 * <ul>
 *   <li>Códec de video: libx264</li>
 *   <li>Códec de audio: aac</li>
 *   <li>Formato de salida: segment</li>
 *   <li>Duración del segmento: 300 segundos</li>
 *   <li>Reinicio de timestamps: 1</li>
 *   <li>Protocolo RTSP: tcp</li>
 * </ul>
 * </p>
 * <h3>Uso:</h3>
 * <pre>
 * public class Main {
 *     public static void main(String[] args) {
 *         try (CameraManager manager = new CameraManager("info.json")) {
 *             // El servicio se ejecuta y gestiona automáticamente dentro del bloque
 *             // Aquí puedes realizar otras operaciones si es necesario
 *         } catch (IOException e) {
 *             e.printStackTrace();
 *         }
 *     }
 * }
 * </pre>
 *
 * @author Carlos Noé Muñoz (cnoemunoz@gmail.com)
 */
public class Main {

    /**
     * Método principal de la aplicación que carga la configuración de las cámaras IP desde un archivo JSON
     * y las procesa utilizando {@link CameraManager}.
     *
     * @param args Argumentos de línea de comandos (no se utilizan en este caso).
     * @throws IOException Si ocurre un error al cargar el archivo de configuración.
     */
    public static void main(String[] args) throws IOException {
        try(CameraManager cameraManager = new CameraManager("info.json")) {;}
    }
}