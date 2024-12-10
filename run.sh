#!/usr/bin/env bash

set -Eeuo pipefail

IDENTITY=~/.ssh/raspberry-pi
URL=pi@raspberrypi.local
JAR=raspberry-pi-1.0-SNAPSHOT.jar
mvn clean package 
scp -i "$IDENTITY" "target/$JAR" "$URL:/home/pi" 
ssh -i "$IDENTITY" "$URL" java -cp "$JAR" "com.github.maximtereshchenko.$1"
