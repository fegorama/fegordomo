#ifndef POOLSERVER_H
#define POOLSERVER_H

#include <ESPAsyncWebServer.h>
#include "TreatmentSystem.h"
#include "Lights.h"
#include "APIRest.h"

class PoolServer {
    private:
        TreatmentSystem treatmentSystem;
        Lights lights;

        APIRest apiRest;
        

    protected:
        //std::unique_ptr<AsyncWebServer> server;
        AsyncWebServer server;

        int getIdFromURL(AsyncWebServerRequest *request, String root);
        String getBodyContent(uint8_t *data, size_t len);

    public:
    
        PoolServer();
        void health(AsyncWebServerRequest *request);
        void notFound(AsyncWebServerRequest *request);
        void postRequest(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total);
        void initServer();
};

#endif
