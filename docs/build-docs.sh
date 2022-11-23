#!/usr/bin/sh
#npx openapi-merge-cli --config openapi-merge.yaml
docker run --rm -v `pwd`/.:/app -it joskfg/npx openapi-merge-cli --config openapi-merge.yaml