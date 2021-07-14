#include <Arduino.h>
#include "TreatmentSystem.h"

TreatmentSystem::TreatmentSystem() {
    setGPIOConfig(TREATMENT_SYSTEM_GPIO);
    pinMode(gpio, OUTPUT);
Serial.println(gpio);
}

int  TreatmentSystem::getGPIOConfig() {
    return gpio;
}

void TreatmentSystem::setGPIOConfig(int gpio) {
    this->gpio = gpio;
}

void TreatmentSystem::treatmentSystemOn() {
Serial.print("turn on for gpio "); Serial.println(gpio);
    digitalWrite(gpio, HIGH);
}

void TreatmentSystem::treatmentSystemOff() {
    digitalWrite(gpio, LOW);
}