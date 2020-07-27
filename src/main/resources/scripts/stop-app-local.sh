#!/bin/bash

# shellcheck disable=SC2164
cd "$1"
pwd
cd "target/local-deploy/scripts"
pwd
echo "Bye Bye ...!!!!!!!!!"
read -r _pid < boot-app-local.pid
echo Killing App with PID "$_pid"
kill -9 "$_pid"

