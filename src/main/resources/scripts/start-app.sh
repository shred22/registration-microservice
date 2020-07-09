#!/bin/bash
echo "!!!!!!!!!... Executing Start Script ...!!!!!!!"
java -cp "lib/*" com.oai3.StartApplication --spring.config.location=config/ & echo "$!" >> boot-app.pid