#!/usr/bin/sh
#mkdir raw
#curl --location --request GET 'localhost:8081/v3/api-docs.yaml' > raw/token.yaml
#curl --location --request GET 'localhost:8082/v3/api-docs.yaml' > raw/users.yaml
#curl --location --request GET 'localhost:8083/v3/api-docs.yaml' > raw/locks.yaml
#curl --location --request GET 'localhost:8084/v3/api-docs.yaml' > raw/orders.yaml

curl --location --request GET 'localhost:8100/v3/api-docs.yaml' > ../storoom.api-docs.yaml