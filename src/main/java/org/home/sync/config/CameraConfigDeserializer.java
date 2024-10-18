package org.home.sync;

import com.google.gson.*;
import org.home.sync.recording.StreamQuality;

import java.lang.reflect.Type;
import java.net.UnknownHostException;

/**
 * Deserializador personalizado para CameraConnectionInfo,permite
 * cargar las propiedades de streaming opcionales desde el JSON.
 *
 * @author Carlos Noé Muñoz (cnoemunoz@gmail.com).
 */
public class CameraConfigDeserializer implements JsonDeserializer<CameraConfig> {

    @Override
    public CameraConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String user = jsonObject.get("user").getAsString();
        String password = jsonObject.get("password").getAsString();
        String ip = jsonObject.get("ip").getAsString();
        int port = jsonObject.get("port").getAsInt();
        String stream = jsonObject.get("stream").getAsString();
        StreamQuality streamQuality = StreamQuality.valueOf(stream.toUpperCase());
        // Crear la instancia de CameraConnectionInfo con las propiedades obligatorias
        CameraConfig cameraConfig = null;
        try {
            cameraConfig = new CameraConfig(name, user, password, ip, port, streamQuality);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // Configurar los valores opcionales, si están presentes en el JSON
        if (jsonObject.has("videoCodec")) {
            cameraConfig.setVideoCodec(jsonObject.get("videoCodec").getAsString());
        }

        if (jsonObject.has("audioCodec")) {
            cameraConfig.setAudioCodec(jsonObject.get("audioCodec").getAsString());
        }

        if (jsonObject.has("format")) {
            cameraConfig.setFormat(jsonObject.get("format").getAsString());
        }

        if (jsonObject.has("segmentTime")) {
            cameraConfig.setSegmentTime(jsonObject.get("segmentTime").getAsString());
        }

        if (jsonObject.has("resetTimeStamps")) {
            cameraConfig.setResetTimeStamps(jsonObject.get("resetTimeStamps").getAsString());
        }

        if (jsonObject.has("RTSPTransport")) {
            cameraConfig.setRTSPTransport(jsonObject.get("RTSPTransport").getAsString());
        }

        return cameraConfig;
    }
}