#!/bin/bash
echo "!!!!!!!!!... Pushing docker image ...!!!!!!!"
pwd
echo "$2"
docker push shred22/docker-regservice:"$2"
sleep 20