#!/bin/bash
# Configuración de proxy para MyST
export http_proxy=http://proxy.cnea.gob.ar:3128
export https_proxy=http://proxy.cnea.gob.ar:3128
export HTTP_PROXY=http://proxy.cnea.gob.ar:3128
export HTTPS_PROXY=http://proxy.cnea.gob.ar:3128
export no_proxy=localhost,127.0.0.1
export NO_PROXY=localhost,127.0.0.1

exec /home/mrtin/.nvm/versions/node/v22.18.0/bin/myst "$@"
