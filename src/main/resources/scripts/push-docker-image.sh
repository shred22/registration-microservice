#!/bin/bash
echo "!!!!!!!!!... Pushing docker image ...!!!!!!!"
pwd
echo "$2"
docker push shred22/regservice:"$2"
sleep 20