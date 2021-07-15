#include <ArduinoJson.h>
#include "WebServer.h"
#include "APIRest.h"

WebServer::WebServer() : server(80) {}

void WebServer::initServer()
{
    server.on("/health", HTTP_GET, std::bind(&APIRest::health, apiRest, std::placeholders::_1));
    server.on(
        "/gpio", HTTP_POST, [](AsyncWebServerRequest *request) {}, NULL,
        std::bind(&APIRest::gpio, apiRest,
                  std::placeholders::_1, std::placeholders::_2, std::placeholders::_3,
                  std::placeholders::_4, std::placeholders::_5));

    server.onNotFound(std::bind(&APIRest::notFound, apiRest, std::placeholders::_1));

    server.begin();
    Serial.println("HTTP server started");
}
