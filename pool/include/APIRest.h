#ifndef APIREST_H
#define APIREST_H
#include <ESPAsyncWebServer.h>
#include "TreatmentSystem.h"
#include "Lights.h"

class APIRest {
    private:
        TreatmentSystem treatmentSystem;
        Lights lights;

        int getIdFromURL(AsyncWebServerRequest *request, String root);
        String getBodyContent(uint8_t *data, size_t len);

    public:
        APIRest();

        void postRequest(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total);
};

#endif