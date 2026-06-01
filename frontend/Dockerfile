# syntax=docker/dockerfile:1
FROM node:22-alpine

ENV TZ=UTC
RUN apk add --no-cache bash curl npm yarn g++ make git
WORKDIR /app
EXPOSE 4200
ENTRYPOINT ["sh", "start.sh"]
