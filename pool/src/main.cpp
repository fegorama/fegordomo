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
