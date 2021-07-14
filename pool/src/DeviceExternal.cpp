#include <Arduino.h>
#include "DeviceExternal.h"

DeviceExternal::DeviceExternal(String id, String name, byte gpio, byte type, String schedule) {
    this->id = id;
    this->name = name;
    this->gpio = gpio;
    this->type = type;
    this->schedule = schedule;

    pinMode(gpio, type);
}

void DeviceExternal::switchOn() {
    digitalWrite(gpio, HIGH);
}

void DeviceExternal::switchOff() {
    digitalWrite(gpio, LOW);
}