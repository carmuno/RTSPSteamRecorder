# HouseSync

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




