/**
 * @file 	WebServer.cpp
 * @brief 	Servidor Web
 *
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 *
 * Licensed under the EUPL-1.2-or-later
 */
#include <ArduinoJson.h>
#include "WebServer.h"
#include "APIRest.h"

WebServer::WebServer() : server(80) {}

/**
 * Inicio del servicio web
 */
void WebServer::initServer()
{
    server.on("/health", HTTP_GET, std::bind(&APIRest::health, apiRest, std::placeholders::_1));
    server.on("/main", HTTP_GET, std::bind(&APIRest::mainPage, apiRest, std::placeholders::_1));
    server.on("/connection", HTTP_GET, std::bind(&APIRest::connectionConfig, apiRest, std::placeholders::_1));
    server.on(
        "/saveConfig", HTTP_POST, [](AsyncWebServerRequest *request) {}, NULL,
        std::bind(&APIRest::saveConfig, apiRest,
                  std::placeholders::_1, std::placeholders::_2, std::placeholders::_3,
                  std::placeholders::_4, std::placeholders::_5));
server.on(
        "/message", HTTP_POST, [](AsyncWebServerRequest *request) {}, NULL,
        std::bind(&APIRest::message, apiRest,
                  std::placeholders::_1, std::placeholders::_2, std::placeholders::_3,
                  std::placeholders::_4, std::placeholders::_5));                  
    server.on(
        "/gpio", HTTP_POST, [](AsyncWebServerRequest *request) {}, NULL,
        std::bind(&APIRest::gpio, apiRest,
                  std::placeholders::_1, std::placeholders::_2, std::placeholders::_3,
                  std::placeholders::_4, std::placeholders::_5));

    server.onNotFound(std::bind(&APIRest::notFound, apiRest, std::placeholders::_1));

    server.begin();
    Serial.println("HTTP server started");
}
