/**
 * @file 	main.cpp
 * @brief 	Entrada principal al programa
 * 
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */

//#include <ESPAsyncWebServer.h>
#include <SPIFFS.h>
#include <ArduinoJson.h>

//#include "WebServer.h"
#include "Config.h"
#include "WebSecServer.h"
#include "UtilsConnection.h"
#include "MqttClient.h"

WebSecServer *webSecServer = new WebSecServer();
MqttClient *mqttClient = new MqttClient();
Config *config = new Config();

void setup() {
	Serial.begin(115200);
	Serial.setTimeout(500);

	// config->save();
	config->load();
	ConnectWiFi_STA(true);

//	webSecServer->initServer();

	/* MQTT Client
	 */
	mqttClient->init();
	mqttClient->reconnect();
}

void loop() {
	/* MQTT Client Service
	*/
	if (!mqttClient->isConnected()) {
		mqttClient->reconnect();
	}

	mqttClient->loop();

	/* Web Secure Server
	*/
//	webSecServer->secureServer->loop();
	delay(1);
}
