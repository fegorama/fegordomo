#include <ArduinoJson.h>
#include "PoolServer.h"
#include "APIRest.h"

void helloWorld(AsyncWebServerRequest *request) {
    request->send(200, "text/plain", "Hello, World!");
}

PoolServer::PoolServer() : server(80) {
	//server.reset(new AsyncWebServer(8888));
}

int PoolServer::getIdFromURL(AsyncWebServerRequest *request, String root) {
    String string_id = request->url();
    string_id.replace(root, "");
    int id = string_id.toInt();
    return id;
}

String PoolServer::getBodyContent(uint8_t *data, size_t len) {
    String content = "";
    for (size_t i = 0; i < len; i++) {
        content .concat((char)data[i]);
    }
    return content;
}

void PoolServer::health(AsyncWebServerRequest *request) {
    Serial.println("/health");
    request->send(200, "text/plain", "{\"health\" : \"OK\"}");
}

void PoolServer::notFound(AsyncWebServerRequest *request) {
	request->send(404, "text/plain", "Service not found");
}

void PoolServer::postRequest(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total) { 
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

    String message = "OK!!";
    Serial.println(message);
    request->send(200, "text/plain", message);
}

void PoolServer::initServer() {
server.on("/", HTTP_GET, [](AsyncWebServerRequest * request){
    Serial.println("/");
    request->send(200, "text/plain", "OK");
});    
//	server.on("/", helloWorld);

    server.on("/health", HTTP_GET, std::bind(&PoolServer::health, this, std::placeholders::_1));

    //server.on("/health", HTTP_GET, [](AsyncWebServerRequest * request){}, NULL, std::bind(&PoolServer::health, this, std::placeholders::_1));

    server.on("/pool", HTTP_POST, [](AsyncWebServerRequest * request){}, NULL, 
            std::bind(&APIRest::postRequest, apiRest, 
            std::placeholders::_1, std::placeholders::_2, std::placeholders::_3, 
            std::placeholders::_4, std::placeholders::_5));

    //    server->on("/pool", HTTP_POST, [](AsyncWebServerRequest * request){}, NULL, 
    //        std::bind(&PoolServer::postRequest, this, 
    //        std::placeholders::_1, std::placeholders::_2, std::placeholders::_3, 
    //        std::placeholders::_4, std::placeholders::_5));
	//server->on("/pool", HTTP_POST, [](AsyncWebServerRequest * request){}, NULL, std::bind(&PoolServer::postRequest, this, std::placeholders::_1));
	//server.on("/item", HTTP_POST, [](AsyncWebServerRequest * request){}, NULL, postRequest);
	//server.on("/item", HTTP_PUT, [](AsyncWebServerRequest * request){}, NULL, putRequest);
	//server.on("/item", HTTP_PATCH, [](AsyncWebServerRequest * request){}, NULL, patchRequest);
	//server.on("/item", HTTP_DELETE, deleteRequest);
	
	server.onNotFound(std::bind(&PoolServer::notFound, this, std::placeholders::_1));

	server.begin();
    Serial.println("HTTP server started");
}
