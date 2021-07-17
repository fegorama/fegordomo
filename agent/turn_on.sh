#!/bin/bash
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"treatmentSystem":"on","lights":"on"}' \
  http://192.168.1.200/pool
