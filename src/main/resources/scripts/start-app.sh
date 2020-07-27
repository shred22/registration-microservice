#!/bin/bash
echo "!!!!!!!!!... Executing Start Script ...!!!!!!!"
touch boot-app.pid
chmod 777 boot-app.pid
cd ..
touch app-startup.logs
chmod 777 app-startup.logs
nohup java -jar registration-service.jar --spring.config.location=config/ > app-startup.logs & echo "$!" > ./scripts/boot-app.pid
