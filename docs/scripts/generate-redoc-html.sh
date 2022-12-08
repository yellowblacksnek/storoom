#!/usr/bin/sh

curl --location --request GET 'localhost:8100/v3/api-docs.yaml' > ../storoom.api-docs.yaml

#npx redoc-cli build output.yaml
docker run --user $(id -u):$(id -g) --rm -v `pwd`/../:/app -it joskfg/npx redoc-cli build storoom.api-docs.yaml