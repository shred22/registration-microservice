#!/bin/bash
echo "!!!!!!!!!... Executing Start Script ...!!!!!!!"
touch boot-app.pid
chmod 777 boot-app.pid
cd ..
java -cp "lib/*" com.oai3.StartApplication --spring.config.location=config/ & echo "$!" > ./scripts/boot-app.pid