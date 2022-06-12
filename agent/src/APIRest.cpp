/**
 * @file 	APIRest.cpp
 * @brief 	API de llamadas a RESTful
 * 
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#include <ESPAsyncWebServer.h>
#include <ArduinoJson.h>
#include <Stream.h>
#include "esp_log.h"
#include "APIRest.h"

/**
 * Métodos privados
 */

int APIRest::getIdFromURL(AsyncWebServerRequest *request, String root)
{
    String string_id = request->url();
    string_id.replace(root, "");
    int id = string_id.toInt();
    return id;
}

String APIRest::getBodyContent(uint8_t *data, size_t len)
{
    String content = "";

    for (size_t i = 0; i < len; i++)
    {
        content.concat((char)data[i]);
    }

    return content;
}

/**
 * Métodos públicos
 */

APIRest::APIRest() {}

void APIRest::notFound(AsyncWebServerRequest *request)
{
    request->send(404, "text/plain", "Service not found");
}

void APIRest::health(AsyncWebServerRequest *request)
{
    Serial.println("/health");
    request->send(200, "text/plain", "{\"health\" : \"OK\"}");
}

void APIRest::mainPage(AsyncWebServerRequest *request)
{
    const char *html = "<html>\n\
    <head>\n\
    <title>Admin</title>\n\
    </head>\n\
    <body>\n\
    <p>Fegordomo administration</p>\n\
    </body>\n\
    </html>\n\
    \n";

    ESP_LOGI(TAG, "Get main page");
    request->send(200, "text/html", html);
}

void APIRest::gpio(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total)
{
    String bodyContent = getBodyContent(data, len);

    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, bodyContent);

    if (error)
    {
        request->send(400);
        return;
    }

    const byte gpio = doc["gpio"];
    const byte mode = doc["mode"];
    const byte action = doc["action"];

    pinMode(gpio, mode);
    digitalWrite(gpio, action);

    Serial.println(bodyContent);
    request->send(200, "application/json", bodyContent);
}
