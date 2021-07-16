/**
 * @file 	WebServer.h
 * @brief 	Servidor Web
 * 
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef POOLSERVER_H
#define POOLSERVER_H

#include <ESPAsyncWebServer.h>
#include "APIRest.h"

class WebServer
{
private:
    APIRest apiRest;
    AsyncWebServer server;

    int getIdFromURL(AsyncWebServerRequest *request, String root);
    String getBodyContent(uint8_t *data, size_t len);

public:
    WebServer();
    void initServer();
};

#endif
