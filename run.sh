#!/usr/bin/env bash

set -Eeuo pipefail

URL=pi@raspberrypi.local
JAR=raspberry-pi-1.0-SNAPSHOT.jar
mvn clean package 
scp "target/$JAR" "$URL:/home/pi" 
ssh "$URL" sudo java -cp "$JAR" "com.github.maximtereshchenko.$1"
