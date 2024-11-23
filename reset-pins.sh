#!/usr/bin/env bash

set -Eeuo pipefail

IDENTITY=~/.ssh/raspberry-pi
URL=pi@raspberrypi.local
ssh -i "$IDENTITY" "$URL" <<EOF
for i in {0..31}
do
  gpio mode "\$i" in
  gpio write "\$i" 0
done
EOF
