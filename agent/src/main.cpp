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
#include "Config.h"
#include "WebSecServer.h"
#include "WebServer.h"
#include "UtilsConnection.h"
#include "MqttClient.h"

#define LOG_LOCAL_LEVEL ESP_LOG_INFO
#include "esp_log.h"

static char *TAG = "FEGORDOMO";

WebSecServer *webSecServer = new WebSecServer();
WebServer *webServer = new WebServer();
MqttClient *mqttClient = new MqttClient();
Config *config = new Config();

void setup() {
	esp_log_level_set("*", ESP_LOG_INFO);

	ESP_LOGI(TAG, "Application Startup..");
    ESP_LOGI(TAG, "Free memory: %d bytes", esp_get_free_heap_size());
    ESP_LOGI(TAG, "IDF version: %s", esp_get_idf_version());

	Serial.begin(115200);
	Serial.setTimeout(500);

	//config->save();
	ESP_LOGI(TAG, "Read config");
	config->load();
	ConnectWiFi_STA(true);

	ESP_LOGI(TAG, "WebServer init");
	webSecServer->initServer();
	webServer->initServer();

	/* MQTT Client
	 */
	ESP_LOGI(TAG, "MQTT init");
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
	//webSecServer->secureServer->loop();
	delay(10);
}
