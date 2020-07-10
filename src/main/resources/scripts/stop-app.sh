#!/bin/bash

echo "Bye Bye ...!!!!!!!!!"
read -r _pid < boot-app.pid
echo "Killing App with PID $_pid"
kill -9 "$_pid"

