#!/bin/bash
echo "!!!!!!!!!... Building and tagging docker image ...!!!!!!!"
pwd
echo $2
docker build -f Dockerfile -t shred22/docker-regservice:$2 -t shred22/docker-regservice:latest .
sleep 20