#ifdef ESP32
#include <WiFi.h>
#else
#include <ESP8266WiFi.h>
#endif
#include <ESPAsyncWebServer.h>
#include <SPIFFS.h>
#include <ArduinoJson.h>

#include "config.h"  
#include "API.hpp"
#include "Server.hpp"
#include "ESP32_Utils.hpp"

void setup() {
	pinMode(LED_GPIO, OUTPUT);
	Serial.begin(115200);

	ConnectWiFi_STA(true);

	InitServer();
}

void loop() {
}
