#include <ESPAsyncWebServer.h>
#include <ArduinoJson.h>
#include <Stream.h>
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
Serial.println("post");
Serial.println(ts);
Serial.println(lhts);

    if (strcasecmp(ts, "on") == 0) {
        this->treatmentSystem.treatmentSystemOn();
Serial.println("depuradora on");       
    } else if (strcasecmp(ts, "off") == 0) {
Serial.println("depuradora off");       
        this->treatmentSystem.treatmentSystemOff();
    }

    if (strcasecmp(lhts, "on") == 0) {
        this->lights.lightsOn();
Serial.println("luces on");       
    } else if(strcasecmp(lhts, "off") == 0) {
Serial.println("luces off");       
        this->lights.lightsOff();
    }

    String message = "OK";
    Serial.println(message);
    request->send(200, "text/plain", message);
}