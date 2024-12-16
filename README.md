# RTSPSteamRecorder

📹 **Simple Java library to capture and store RTSP streams**.

### Key Features:
- 📂 Stores recordings in a directory.
- ⚙️ Easy configuration using a JSON file.
- 🎥 Supports multiple cameras at the same time.
- 📁 Recordings are saved in a folder named after each camera (as specified in the JSON).

example: (only in local access)

```json
[
  {
    "name": "backyard-garden",
    "user": "cnoe-tapo",
    "password": "bypvos-7qycme-cejfiF",
    "ip": "192.168.1.50",
    "port": 554,
    "stream": "stream2",
    "segmentTime": 150
  },
  {
    "name": "frontyard-garden",
    "user": "cnoe-tapo",
    "password": "bypvos-7qycme-cejfiF",
    "ip": "192.168.1.42",
    "port": 554,
    "stream": "stream2",
    "segmentTime": 150
  }
]

```

### JSON Configuration Explained

- **name**: The unique name you assign to the camera. This name will also be used as the directory name where the recordings are stored.
    - Example: `"name": "backyard-garden"`

- **user**: The username required to access the camera’s RTSP stream. This should match the credentials set up in the camera's configuration.
    - Example: `"user": "cnoe-tapo"`

- **password**: The password required to authenticate the RTSP connection. This should match the camera's configuration and is needed to securely access the stream.
    - Example: `"password": "bypvos-7qycme-cejfiF"`

- **ip**: The IP address of the camera on your local network. Make sure the IP is correct and static, so it doesn't change over time.
    - Example: `"ip": "192.168.1.50"`

- **port**: The port used to connect to the camera's RTSP stream. Typically, RTSP runs on port 554, but this can vary depending on your camera setup.
    - Example: `"port": 554`

- **stream**: The specific stream you want to capture from the camera. Cameras often provide multiple streams (e.g., high and low quality).
    - Example: `"stream": "stream2"`

- **segmentTime**: The duration in seconds for each video segment when storing the recorded footage. This defines how long each file will be before starting a new one.
    - Example: `"segmentTime": 150` (which means each segment will be 150 seconds long)

# Camera Recording Configuration

In addition to the mandatory parameters (`name`, `user`, `password`, `ip`, `port`, `stream`, and `segmentTime`), the system allows optional configurations in the JSON to customize the recording. Below are the optional parameters that can be used:

### Optional Parameters

- **`videoCodec`**: Specifies the video codec used for conversion and storage. The default value is `"libx264"`.
  - **Example**: `"videoCodec": "libx264"`

- **`audioCodec`**: Specifies the audio codec used for recording. The default value is `"aac"`.
  - **Example**: `"audioCodec": "aac"`

- **`format`**: Defines the output format of the recording file. The default value is `"segment"` to allow segmentation.
  - **Example**: `"format": "segment"`

- **`segmentTime`**: Sets the duration of each video segment in seconds. If specified, it overrides the default value.
  - **Example**: `"segmentTime": 300`

- **`resetTimeStamps`**: Configures whether the timestamps are reset in each new segment. The default value is `"1"`.
  - **Example**: `"resetTimeStamps": "1"`

- **`RTSPTransport`**: Specifies the transport protocol used for RTSP streaming. The default value is `"tcp"`.
  - **Example**: `"RTSPTransport": "tcp"`

### Complete JSON Configuration Example

Here is a complete JSON configuration example for two cameras, using both mandatory and optional parameters:

```json
[
  {
    "name": "backyard-garden",
    "user": "cnoe-tapo",
    "password": "password",
    "ip": "192.168.1.50",
    "port": 554,
    "stream": "stream2",
    "segmentTime": 150,
    "videoCodec": "libx264",
    "audioCodec": "aac",
    "format": "segment",
    "resetTimeStamps": "1",
    "RTSPTransport": "tcp"
  },
  {
    "name": "frontyard-garden",
    "user": "cnoe-tapo",
    "password": "password",
    "ip": "192.168.1.42",
    "port": 554,
    "stream": "stream2",
    "segmentTime": 150,
    "videoCodec": "libx264",
    "audioCodec": "aac",
    "format": "segment",
    "resetTimeStamps": "1",
    "RTSPTransport": "tcp"
  }
]
