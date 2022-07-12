/**
 * @file 	NetConnection.cpp
 * @brief 	Utilidades de conexión (wifi, ethernet, etc.).
 *
 * @author 	Fernando González (Fegor)
 * @date 	  12/07/2021
 * @version 1.0.0
 *
 * Licensed under the EUPL-1.2-or-later
 */
#ifdef ESP32
#include <WiFi.h>
#else
#include <ESP8266WiFi.h>
#endif

#ifndef CONFIG_H
#include "Config.h"
#endif

#include "esp_log.h"
#include "NetConnection.h"

const String NetConnection::TAG = "NetConnection";

NetConnection *NetConnection::netConnection_ = nullptr;

NetConnection::NetConnection() 
{
    // Constructor privado
}

NetConnection *NetConnection::getInstance()
{
    if (netConnection_ == nullptr)
    {
        netConnection_ = new NetConnection();
    }

    return netConnection_;
}

/**
 * Conexión por Wifi en modo STA
 */
void NetConnection::connectWiFi_STA(bool useStaticIP)
{
    delay(100);
    WiFi.begin(Config::wifiSSID.c_str(), Config::wifiPassword.c_str());

    if (useStaticIP)
    {
        staticIP.fromString(Config::staticIP);
        staticGW.fromString(Config::staticGW);
        staticSN.fromString(Config::staticSN);
        WiFi.config(staticIP, staticGW, staticSN);
    }

    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
    }

    randomSeed(micros());
    ESP_LOGI(TAG, "Init STA: %s with IP address: %s", Config::wifiSSID, WiFi.localIP().toString());
}

/**
 * Conexión por Wifi en modo AP
 */
void NetConnection::connectWiFi_AP(bool useStaticIP)
{
    WiFi.mode(WIFI_AP);

    while (!WiFi.softAP(Config::wifiSSID.c_str(), Config::wifiPassword.c_str()))
    {
        delay(500);
    }

    if (useStaticIP)
    {
        staticIP.fromString(Config::staticIP);
        staticGW.fromString(Config::staticGW);
        staticSN.fromString(Config::staticSN);
        WiFi.softAPConfig(staticIP, staticGW, staticSN);
    }

    ESP_LOGI(TAG, "Init AP: %s with IP address: %s", Config::wifiSSID, WiFi.softAPIP().toString());
}

