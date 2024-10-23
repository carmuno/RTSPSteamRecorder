# HouseSync

A very simple Java library that captures RTSP streams and stores them in a directory. The configuration is straightforward, using a JSON file to specify camera parameters. The library supports downloading and storing video files from multiple cameras simultaneously, providing an efficient and easy-to-use solution for managing RTSP streams. The result of the recording is stored by default in a directory whose name is the name of the camera you specified in the JSON.

example: (only in local access)

```json
[
  {
    "name": "jardin-trasero",
    "user": "cnoe-tapo",
    "password": "bypvos-7qycme-cejfiF",
    "ip": "192.168.1.50",
    "port": 554,
    "stream": "stream2",
    "segmentTime": 150
  },
  {
    "name": "jardin-delantero",
    "user": "cnoe-tapo",
    "password": "bypvos-7qycme-cejfiF",
    "ip": "192.168.1.42",
    "port": 554,
    "stream": "stream2",
    "segmentTime": 150
  }
]




