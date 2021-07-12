#include <WiFi.h>
#include <ESPAsyncWebServer.h>
#include <SPIFFS.h>
#include <ArduinoJson.h>

#include "config.h"  // Sustituir con datos de vuestra red
#include "API.hpp"
#include "Server.hpp"
#include "ESP32_Utils.hpp"

void setup() 
{
	pinMode(LED_GPIO, OUTPUT);
	Serial.begin(115200);

	ConnectWiFi_STA(true);

	InitServer();
}

void loop() 
{
}
