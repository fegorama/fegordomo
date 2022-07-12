/**
 * @file 	APIRest.h
 * @brief 	API de llamadas a RESTful
 * 
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef APIREST_H
#define APIREST_H

//#include <ESPAsyncWebServer.h>

extern String processor(const String &var);

class APIRest
{
private:
    const char* TAG = "APIRest";

    int getIdFromURL(AsyncWebServerRequest *request, String root);
    String getBodyContent(uint8_t *data, size_t len);

public:
    APIRest();
    void health(AsyncWebServerRequest *request);
    void saveConfig(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total);
    void mainPage(AsyncWebServerRequest *request);
    void connectionConfig(AsyncWebServerRequest *request);
    void saveConnection(AsyncWebServerRequest *request);
    void messageManager(AsyncWebServerRequest *request);
    void gpio(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total);
    void notFound(AsyncWebServerRequest *request);
};

#endif