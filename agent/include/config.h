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

using namespace std;
class Config {
    private:
        const char* FILE_NAME = "/config.json" ;

        string wifiSSID = "FIDONET";
        string wifiPassword = "Manual_de_BASIC2";
        string wifiHostname = "ESP32_POOL";

        char staticIP[16] = "192.168.1.200";
        char staticGW[16] = "192.168.1.1";
        char staticSN[16] = "255.255.255.0";

        string mqttUsername = "fegordomo";
        string mqttPassword = "fegordomo";

    public:
        Config();
        void load();
        void save();
};
#endif
