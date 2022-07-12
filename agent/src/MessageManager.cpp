#include <ArduinoJson.h>
#include <Stream.h>
#include <stdio.h>
#include <string.h>
#include "main.h"
#include "MessageManager.h"

#define MAX_LENGTH_DOC 1024

MessageManager::MessageManager()
{
}

void MessageManager::proccess(char *topic, byte *payload, unsigned int length)
{
  Serial.println("Message arrived:");
  Serial.print("Topic: ");
  Serial.println(topic);

  if (length > MAX_LENGTH_DOC)
  {
    mqttClient->pub("Message too long!");
    return;
  }

  // taticJsonDocument<200> doc;
  DynamicJsonDocument doc(1024);
  // DeserializationError error = deserializeJson(doc, msg);
  DeserializationError error = deserializeJson(doc, payload, length);

  if (error)
  {
    mqttClient->pub("Error in message (json)!");
    return;
  }

  Serial.println("Deserialized message...");

  const uint8_t mode = 2;
  const char *device = doc["deviceName"];
  const char *data = doc["data"];

  Serial.print("Device: ");
  Serial.println(device);
  Serial.print("data: ");
  Serial.println(data);

  uint8_t pin = 0;
  uint8_t action = 0;

  this->findDeviceData(device, data, pin, action);
  
  if (pin != 0)
  {
    pinMode(pin, mode);
    digitalWrite(pin, action);
    mqttClient->pub("Message proccessed successful");
  }
  else
  {
    Serial.print("Device not found: ");
    Serial.println(device);
  }
}

void MessageManager::findDeviceData(const String device, const String data, uint8_t &pin, uint8_t &action)
{
  for (uint8_t i = 0; i < sizeof(message) / sizeof(Message); i++)
  {
    if (strcmp(message[i].device.c_str(), device.c_str()) && strcmp(message[i].data.c_str(), data.c_str()))
    {
      pin = message[i].pin;
      action = message[i].action;
      break;
    }
  }
}
