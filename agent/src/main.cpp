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

#include <ESPAsyncWebServer.h>
#include <SPIFFS.h>
#include <ArduinoJson.h>

#include "WebServer.h"
#include "UtilsConnection.h"

void setup()
{
	Serial.begin(115200);
	ConnectWiFi_STA(true);
	WebServer *webServer = new WebServer();
	webServer->initServer();
}

void loop()
{
}
