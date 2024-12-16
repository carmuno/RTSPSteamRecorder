#!/bin/bash
# Para descargar el binary del mediamtx. Necesario por si se realizan operaciones de duplicación de RTSP.
echo "Detectando sistema operativo..."
OS=$(uname -s)
ARCH=$(uname -m)
URL=""

# Determinar URL de descarga según el sistema operativo y arquitectura. incluir nuevas versiones y SO si se necesita...
# Yo solo he puesto las que a mi me afectan.
if [ "$OS" = "Darwin" ] && [ "$ARCH" = "arm64" ]; then  # Para macOS con el procesador nuevo.
    URL="https://github.com/bluenviron/mediamtx/releases/download/v1.10.0/mediamtx_v1.10.0_darwin_arm64.tar.gz"
elif [ "$OS" = "Darwin" ] && [ "$ARCH" = "x86_64" ]; then # Para macOS con el procesador antiguo.
    URL="https://github.com/bluenviron/mediamtx/releases/download/v1.10.0/mediamtx_v1.10.0_darwin_amd64.tar.gz"
elif [ "$OS" = "Linux" ] && [ "$ARCH" = "arm64" ]; then  # Para unix (ARM).
    URL="https://github.com/bluenviron/mediamtx/releases/download/v1.10.0/mediamtx_v1.10.0_linux_arm64v8.tar.gz"
elif [ "$OS" = "Linux" ] && [ "$ARCH" = "x86_64" ]; then  # Para unix (ARM).
    URL="https://github.com/bluenviron/mediamtx/releases/download/v1.10.0/mediamtx_v1.10.0_linux_amd64.tar.gz"
else
    echo "Sistema no soportado. Por favor descarga mediamtx manualmente."
    exit 1
fi

# Verificar si mediamtx ya está instalado
if command -v /tmp/rtspSimpleServer/mediamtx &>/dev/null; then
    echo "mediamtx ya está instalado en el sistema."
    echo "Ubicación: $(which /tmp/rtspSimpleServer/mediamtx)"
    echo "Versión:"
    /tmp/rtspSimpleServer/mediamtx --version
    echo "Si quieres reinstalar, elimina la instalación existente y vuelve a ejecutar este script."
    exit 0
fi

cd /tmp/

mkdir rtspSimpleServer

cd rtspSimpleServer

echo "Descargando mediamtx desde $URL ..."
curl -L -o mediamtx.tar.gz "$URL"

echo "Extrayendo mediamtx..."
tar -xzf mediamtx.tar.gz
rm -rf mediamtx.tar.gz

echo "ejecutando "

./mediamtx

