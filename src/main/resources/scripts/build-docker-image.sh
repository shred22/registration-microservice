#!/bin/bash
echo "!!!!!!!!!... Building and tagging docker image ...!!!!!!!"
pwd
echo $2
docker build -f Dockerfile -t shred22/regservice:$2 .
sleep 20