FROM openapitools/openapi-generator-cli:latest

WORKDIR /openapi

COPY ./generator .

COPY ./docs .

RUN mkdir backend

RUN mkdir frontend