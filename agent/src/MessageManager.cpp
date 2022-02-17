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
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");

  if (length > MAX_LENGTH_DOC) 
  {
    mqttClient->pub("Message too long!");
    return;
  }

  StaticJsonDocument<200> doc;
  //DeserializationError error = deserializeJson(doc, msg);
  DeserializationError error = deserializeJson(doc, payload, length);

  if (error)
  {
    mqttClient->pub("Error in message (json)!");
    return;
  }

  const byte gpio = doc["gpio"];
  const byte mode = doc["mode"];
  const byte action = doc["action"];

  pinMode(gpio, mode);
  digitalWrite(gpio, action);
  mqttClient->pub("Message proccessed successful");
}
