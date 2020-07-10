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
cp -r boot-oai-log4j2-zip.zip ./local-deploy
# shellcheck disable=SC2164
cd "local-deploy"
pwd
unzip -o boot-oai-log4j2-zip.zip
echo "Copied ZIP and unzipped bundle"
nohup java -cp "lib/*" com.oai3.StartApplication --spring.config.location=config/ & echo "$!" >> boot-app-local.pid
