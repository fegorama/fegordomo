/**
 * @file 	Config.h
 * @brief 	Configuración general
 * 
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef CONFIG_H
#define CONFIG_H
#include <ArduinoJson.h>

using namespace std;
namespace Config {
    extern const char* FILE_NAME;

    extern String wifiSSID;
    extern String wifiPassword;
    extern String wifiHostname;

    extern String staticIP;
    extern String staticGW;
    extern String staticSN;

    extern String mqttUsername;
    extern String mqttPassword;

    extern boolean setConfig(String strConfig);
    extern DynamicJsonDocument getConfig();
    extern String load();
    extern void save();
};
#endif
