#!/bin/bash
echo "!!!!!!!!!... Executing Start Script ...!!!!!!!"
touch boot-app.pid
chmod 777 boot-app.pid
cd ..
nohup java -jar logging-log4j2-1.0.jar --spring.config.location=config/ >  & echo "$!" > ./scripts/boot-app.pid
