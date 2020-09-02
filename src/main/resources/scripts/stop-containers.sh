#!/bin/bash
echo "!!!!!!!!!... Stopping Containers Reg Service, MySQL and Redis ...!!!!!!!"
#docker container stop docker-mysql docker-redis docker-regservice

docker-compose down
