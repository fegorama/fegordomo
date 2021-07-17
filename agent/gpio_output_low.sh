#!/bin/bash
curl --request GET http://192.168.1.200/health
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"gpio":"26","mode":"2","action":"0"}' \
  http://192.168.1.200/gpio
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"gpio":"27","mode":"2","action":"0"}' \
  http://192.168.1.200/gpio
