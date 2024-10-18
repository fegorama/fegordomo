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
#include <list>
#include "Config.h"
#include "MessageManager.h"

#ifdef ESP32
#include <SPIFFS.h>
#endif

using namespace std;

namespace Config
{
    const char *FILE_NAME = "/config.json";

    String wifiSSID = "IOTNET";
    String wifiPassword = "Qwerty123+";
    String wifiHostname = "ESP32_POOL";

    String staticIP = "172.16.0.101";
    String staticGW = "172.16.0.1";
    String staticSN = "255.255.255.0";

    String mqttUsername = "fegordomo";
    String mqttPassword = "fegordomo";

    std::list<Message> message{
        {27, "pool_light", "on", 1},
        {27, "pool_light", "off", 0},
        {32, "swimming_pool_filter", "on", 0},
        {32, "swimming_pool_filter", "off", 1}};

    boolean setConfig(String strConfig)
    {
        boolean res = false;
        DynamicJsonDocument doc(1024);
        DeserializationError error = deserializeJson(doc, strConfig);
        if (error)
        {
            Serial.print("Error deserializating: ");
            Serial.println(error.c_str());
            return res;
        }
        else
        {
            res = true;
            wifiSSID = doc["wifi"]["wifiSSID"].as<String>();
            wifiPassword = doc["wifi"]["wifiPassword"].as<String>();
            wifiHostname = doc["wifi"]["wifiHostname"].as<String>();
            staticIP = doc["wifi"]["staticIP"].as<String>();
            staticGW = doc["wifi"]["staticGW"].as<String>();
            staticSN = doc["wifi"]["staticSN"].as<String>();
            mqttUsername = doc["mqtt"]["mqttUsername"].as<String>();
            mqttPassword = doc["mqtt"]["mqttPassword"].as<String>();

            message.clear();
            int k = 0;
            for (Message m : message)
            {
                message.push_back({doc["messages"][k]["pin"], doc["messageManager"][k]["device"], doc["messageManager"][k]["data"], doc["messageManager"][k]["action"]});
                k++;
            }
        }

        return res;
    }

    DynamicJsonDocument getConfig()
    {
        DynamicJsonDocument doc(1024);
        doc["wifi"]["wifiSSID"] = wifiSSID;
        doc["wifi"]["wifiPassword"] = wifiPassword;
        doc["wifi"]["wifiHostname"] = wifiHostname;
        doc["wifi"]["staticIP"] = staticIP;
        doc["wifi"]["staticGW"] = staticGW;
        doc["wifi"]["staticSN"] = staticSN;
        doc["mqtt"]["mqttUsername"] = mqttUsername;
        doc["mqtt"]["mqttPassword"] = mqttPassword;

        int k = 0;
        for (Message m : message)
        {
            doc["messages"][k]["pin"] = m.pin;
            doc["messages"][k]["device"] = m.device;
            doc["messages"][k]["data"] = m.data;
            doc["messages"][k]["action"] = m.action;
            k++;
        }

        return doc;
    }

    void save()
    {
        if (SPIFFS.begin(true))
        {
            Serial.println("SPIFFS mounted");
            DynamicJsonDocument doc = getConfig();
            File configFile = SPIFFS.open(FILE_NAME, FILE_WRITE);
            if (configFile)
            {
                String strConfig;
                serializeJson(doc, strConfig);
                Serial.println(strConfig);
                Serial.println("Configuration saved!");
                configFile.print(strConfig);
                // serializeJson(doc, configFile);
                Serial.println(configFile.readString());
                configFile.close();
            }
            else
            {
                Serial.println("Error creating config file!");
            }

            SPIFFS.end();
            Serial.println("SPIFFS unmounted");
        }
        else
        {
            Serial.println("Error mount FS!");
        }
    }

    String load()
    {
        String strConfig;
        strConfig.clear();

        if (SPIFFS.begin(true))
        {
            Serial.println("SPIFFS mounted");
            if (SPIFFS.exists(FILE_NAME))
            {
                File configFile = SPIFFS.open(FILE_NAME, FILE_READ);
                if (configFile)
                {
                    DynamicJsonDocument doc(1024);
                    strConfig = configFile.readString();
                    Serial.println(strConfig);
                    deserializeJson(doc, strConfig);
                    wifiSSID = doc["wifi"]["wifiSSID"].as<String>();
                    wifiPassword = doc["wifi"]["wifiPassword"].as<String>();
                    wifiHostname = doc["wifi"]["wifiHostname"].as<String>();
                    staticIP = doc["wifi"]["staticIP"].as<String>();
                    staticGW = doc["wifi"]["staticGW"].as<String>();
                    staticSN = doc["wifi"]["staticSN"].as<String>();
                    mqttUsername = doc["mqtt"]["mqttUsername"].as<String>();
                    mqttPassword = doc["mqtt"]["mqttPassword"].as<String>();

                    message.clear();
                    int k = 0;
                    for (Message m : message)
                    {
                        message.push_back({doc["messages"][k]["pin"], doc["messageManager"][k]["device"], doc["messageManager"][k]["data"], doc["messageManager"][k]["action"]});
                        k++;
                    }

                    Serial.println("Configuration loaded");
                    configFile.close();
                }
                else
                {
                    Serial.println("Error loading config file!");
                }
            }
            else
            {
                Serial.println("Config file not found!, saving default data");
                save();
            }

            SPIFFS.end();
            Serial.println("SPIFFS umounted");
        }
        else
        {
            Serial.println("Error mount FS!");
        }

        return strConfig;
    }
}
