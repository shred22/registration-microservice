#!/bin/bash
cd ..
java -cp "../lib/*:../logging-log4j2-1.0.jar" com.oai3.StartApplication & echo "$!" >> boot-app.pid