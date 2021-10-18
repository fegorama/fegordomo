/**
 * @file 	Config.cpp
 * @brief 	Configuración
 * 
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */

#include <string>
#include <FS.h>
#include <ArduinoJson.h>
#include "Config.h"

#ifdef ESP32
  #include <SPIFFS.h>
#endif

using namespace std;

Config::Config() { }

void Config::save() {
    Serial.println("Mounting FS...");

    if (SPIFFS.begin(true)) {
        DynamicJsonDocument doc(1024);
        doc["wifiSSID"] = wifiSSID;
        doc["wifiPassword"] = wifiPassword;
        doc["wifiHostname"] = wifiHostname;
        doc["staticIP"] = staticIP;
        doc["staticGW"] = staticGW;
        doc["staticSN"] = staticSN;
        doc["mqttUsername"] = mqttUsername;
        doc["mqttPassword"] = mqttPassword;

        File configFile = SPIFFS.open(FILE_NAME, FILE_WRITE);
        if (configFile) {
            serializeJson(doc, configFile);
            configFile.close();

        } else {
            Serial.println("Error creating config file!");
        }

        SPIFFS.end();

    } else {
        Serial.println("Error mount FS!");
    }
}

void Config::load() {
    Serial.println("Mounting FS...");

    if (SPIFFS.begin(true)) {
        if (SPIFFS.exists(FILE_NAME)) {
             File configFile = SPIFFS.open(FILE_NAME, FILE_READ);
             if (configFile) {
                Serial.println("Load configuration");
                DynamicJsonDocument doc(1024);
                String strConfig = configFile.readString();
                Serial.println(strConfig);
                deserializeJson(doc, strConfig);
                wifiSSID = doc["wifiSSID"].as<string>();
                wifiPassword = doc["wifiPassword"].as<string>();
                wifiHostname = doc["wifiHostname"].as<string>();
                strcpy(staticIP, doc["staticIP"]);
                strcpy(staticGW, doc["staticGW"]);
                strcpy(staticSN, doc["staticSN"]);
                mqttUsername = doc["mqttUsername"].as<string>();
                mqttPassword = doc["mqttPassword"].as<string>();
                configFile.close();

             } else {
                 Serial.println("Error loading config file!");
             }

        } else {
            Serial.println("Config file not found!, saving default data");
            this->save();
        }

        SPIFFS.end();

    } else {
        Serial.println("Error mount FS!");
    }
}