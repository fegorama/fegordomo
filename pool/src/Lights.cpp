#include <Arduino.h>
#include "Lights.h"

Lights::Lights() {
    setGPIOConfig(LIGTHS_GPIO);
    pinMode(this->gpio, OUTPUT);
}

int Lights::getGPIOConfig() {
    return this->gpio;
}

void Lights::setGPIOConfig(int gpio) {
    this->gpio = gpio;
}

void Lights::lightsOn() {
    digitalWrite(this->gpio, HIGH);
}

void Lights::lightsOff() {
    digitalWrite(this->gpio, LOW);
}