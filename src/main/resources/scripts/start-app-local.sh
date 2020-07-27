#!/bin/bash
echo "!!!!!!!!!... Executing Start Script ...!!!!!!!"
# shellcheck disable=SC2164
cd "$1"
pwd
# shellcheck disable=SC2164
cd "target"
pwd
rm -r "local-deploy"
mkdir "local-deploy"
echo "Created dir local-deploy"
cp -r registration-service-bundle-zip.zip ./local-deploy
# shellcheck disable=SC2164
cd "local-deploy"
pwd
unzip -o registration-service-bundle-zip.zip
echo "Copied ZIP and unzipped bundle"
nohup java -jar lib/registration-service.jar --spring.config.location=config/ & echo "$!" > ./scripts/boot-app-local.pid
