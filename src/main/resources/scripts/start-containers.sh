#!/bin/bash
echo "!!!!!!!!!... Starting Docker Containers Reg-Service, MySQL and Redis ...!!!!!!!"
pwd

# Start MYSQL Docker Container
#docker container start docker-mysql

# Start REDIS Docker Container
#docker container start docker-redis

# Start REG-SERVICE Docker Container
#docker container start docker-regservice
docker-compose up -d

sleep 20