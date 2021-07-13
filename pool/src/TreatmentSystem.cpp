#include <Arduino.h>
#include "TreatmentSystem.h"

TreatmentSystem::TreatmentSystem() {
    pinMode(this->gpio, OUTPUT);
}

int  TreatmentSystem::getGPIOConfig() {
    return this->gpio;
}

void TreatmentSystem::setGPIOConfig(int gpio) {
    this->gpio = gpio;
}

void TreatmentSystem::treatmentSystemOn() {
    digitalWrite(this->gpio, HIGH);
}

void TreatmentSystem::treatmentSystemOff() {
    digitalWrite(this->gpio, LOW);
}