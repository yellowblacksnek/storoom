FROM node:alpine as build
WORKDIR /usr/src/app
COPY . ./
#RUN npx openapi-merge-cli --config openapi-merge.yaml
RUN npx redoc-cli build output.yaml

FROM nginx:alpine
COPY --from=build /usr/src/app/redoc-static.html /usr/share/nginx/html/index.html