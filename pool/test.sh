#!/bin/bash
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"gpio":"27","wait":"1000"}' \
  http://192.168.1.200/util/blink
