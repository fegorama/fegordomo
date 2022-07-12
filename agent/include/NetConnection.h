/**
 * @file 	NetConnection.h
 * @brief 	Conexiones de red (ethernet, wifi, etc.)
 *
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 *
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef NETCONNECTION_H
#define NETCONNECTION_H

#ifdef ESP32
#include <WiFi.h>
#else
#include <ESP8266WiFi.h>
#endif

using namespace std;

class NetConnection
{
private:
    static const String TAG;

    NetConnection(const NetConnection &);
    NetConnection &operator=(const NetConnection &);

    IPAddress staticIP;
    IPAddress staticGW;
    IPAddress staticSN;

protected: 
    // Uso de patrón Singleton
    NetConnection();
    static NetConnection *netConnection_;

public:
    static NetConnection *getInstance();
    void connectWiFi_STA(bool useStaticIP = false);
    void connectWiFi_AP(bool useStaticIP = false);
};
#endif