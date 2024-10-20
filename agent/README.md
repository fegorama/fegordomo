# Fegordomo - Agent

http://www.steves-internet-guide.com/mosquitto-tls/
https://medium.com/himinds/mqtt-broker-with-secure-tls-communication-on-ubuntu-18-04-lts-and-an-esp32-mqtt-client-5c25fd7afe67

## Build, upload and monitor

### Upload
    pio run --environment esp32doit-devkit-v1 --target upload

### Monitor
    pio device monitor --environment esp32doit-devkit-v1 --port COM3

## For Windows: Error CP2102 USB to UART Bridge Controller

### Testing for USB devices
    Get-PnpDevice -PresentOnly | Where-Object { $_.InstanceId -match '^USB' }

### Install driver 
    https://www.pololu.com/docs/0j7/all

## Errors

### Error: 
A fatal error occurred: Could not open /dev/ttyUSB0, the port doesn't exist

### Solution:
    adduser <username> dialout
    chmod a+rw /dev/ttyUSB0

