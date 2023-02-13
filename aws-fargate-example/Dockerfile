#use alpine version to decrease docker image size, https://hub.docker.com/_/node?tab=description&page=1&ordering=last_updated
FROM node:18-alpine3.14 As development

WORKDIR /app

COPY package*.json ./

RUN npm install

#copies files from a local source location to a destination in the Docker container
COPY . .

RUN npm run build

CMD ["npm", "run", "start"]

# Multi Staging Build
FROM node:18-alpine3.14 As production

ARG NODE_ENV=production
ENV NODE_ENV=${NODE_ENV}

WORKDIR /app

COPY package*.json ./

# Create node_modules that is necessary only for production
RUN npm install production

#copies files from a local source location to a destination in the Docker container
COPY . .

# Copy dist generated in development stage
COPY --from=development /app/dist ./dist

EXPOSE 3000

CMD ["npm", "run", "start:prod"]