#!/usr/bin/sh
#npx redoc-cli build output.yaml
docker run --rm -v `pwd`/.:/app -it joskfg/npx redoc-cli build output.yaml