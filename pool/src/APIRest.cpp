#include <ESPAsyncWebServer.h>
#include <ArduinoJson.h>
#include "APIRest.h"
#include "TreatmentSystem.h"
#include "Lights.h"

int APIRest::getIdFromURL(AsyncWebServerRequest *request, String root) {
    String string_id = request->url();
    string_id.replace(root, "");
    int id = string_id.toInt();
    return id;
}

String APIRest::getBodyContent(uint8_t *data, size_t len) {
    String content = "";
    for (size_t i = 0; i < len; i++) {
        content .concat((char)data[i]);
    }
    return content;
}

APIRest::APIRest() { 
    this->treatmentSystem.setGPIOConfig(TREATMENT_SYSTEM_GPIO);
    this->lights.setGPIOConfig(LIGTHS_GPIO);
}

void APIRest::postRequest(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total) { 
    String bodyContent = getBodyContent(data, len);
    
    StaticJsonDocument<200> jsonDoc;
    DeserializationError error = deserializeJson(jsonDoc, bodyContent);

    if (error) { 
        request->send(400); 
        return;
    }

    const char *ts = jsonDoc["treatmentSystem"];
    const char *lhts = jsonDoc["lights"];

    if (strcasecmp(ts, "on")) {
        this->treatmentSystem.treatmentSystemOn();
    } else if (strcasecmp(ts, "off")) {
        this->treatmentSystem.treatmentSystemOff();
    }

    if (strcasecmp(lhts, "on")) {
        this->lights.lightsOn();
    } else if(strcasecmp(lhts, "off")) {
        this->lights.lightsOff();
    }
}