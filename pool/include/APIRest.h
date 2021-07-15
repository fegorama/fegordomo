#ifndef APIREST_H
#define APIREST_H

#include <ESPAsyncWebServer.h>

class APIRest
{
private:
    int getIdFromURL(AsyncWebServerRequest *request, String root);
    String getBodyContent(uint8_t *data, size_t len);

public:
    APIRest();
    void health(AsyncWebServerRequest *request);
    void gpio(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total);
    void notFound(AsyncWebServerRequest *request);
};

#endif