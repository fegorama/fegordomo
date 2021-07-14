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
#include "PoolServer.h"
#include "ESP32_Utils.hpp"
#include "DeviceExternal.h"
#include "devices.h"

DeviceExternal *devices;

void build_devices() {
	StaticJsonDocument<1024> doc;
    DeserializationError error = deserializeJson(doc, config_devs);

	if(error) {
		Serial.print(F("deserializeJson() failed with code "));
		Serial.println(error.c_str());
		return;
	}

	JsonArray arr = doc.as<JsonArray>();
	int memoryUsed = doc.memoryUsage();

	for(JsonObject repo : arr) {
		devices = new DeviceExternal(repo["id"],
							repo["name"],
							repo["gpio"],
							repo["type"],
							repo["schedule"]);
	}
}

void setup() {
	//pinMode(LED_GPIO, OUTPUT);
	Serial.begin(115200);

	ConnectWiFi_STA(true);

	PoolServer *poolServer = new PoolServer();
	poolServer->initServer();
}

void loop() {
}
