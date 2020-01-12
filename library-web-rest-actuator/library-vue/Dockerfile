### STAGE 1: Build ###
FROM node:12.7-alpine AS build
RUN mkdir /workspace
WORKDIR /workspace
COPY package.json ./
RUN npm install
COPY . /workspace
CMD ["npm","run","serve"]
