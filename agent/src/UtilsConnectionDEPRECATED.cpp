/**
 * @file 	  UtilsConnection.cpp
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

#include "UtilsConnectionDEPRECATED.h"

#ifndef CONFIG_H
#include "Config.h"
#endif

//#define LOG_LOCAL_LEVEL ESP_LOG_INFO
//#include "esp_log.h"

// TODO Cambiar y usar los datos de configuración
// const char *ssid = "ARPANET";
// const char *password = "Manual_de_BASIC2";
// const char *hostname = "ESP32_POOL";

IPAddress staticIP;
IPAddress staticGW;
IPAddress staticSN;

/**
 * Conexión por Wifi en modo STA
 */
void ConnectWiFi_STA(bool useStaticIP = false)
{
  delay(10);
  // WiFi.mode(WIFI_STA);
  // ESP_LOGI(TAG, "Connecting to wifi in STA mode...");
  Serial.println("Connecting to wifi in STA mode...");
  // WiFi.begin(ssid, password);

  Serial.println(Config::wifiSSID.c_str());
  Serial.println(Config::wifiPassword.c_str());
  Serial.println(Config::staticIP);
  Serial.println(staticIP.fromString(Config::staticIP));

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
  // ESP_LOGI(TAG, "Init STA: %s with IP address: %s", ssid, WiFi.localIP().toString());
  Serial.print("Init STA with IP address: ");
  Serial.println(WiFi.localIP().toString());
}

/**
 * Conexión por Wifi en modo AP
 */
void ConnectWiFi_AP(bool useStaticIP = false)
{
  // ESP_LOGI(TAG, "Connecting to wifi in AP mode...");
  Serial.println("Connecting to wifi in AP mode...");
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

  // ESP_LOGI(TAG, "Init AP: %s with IP address: %s", ssid, WiFi.softAPIP().toString());
  Serial.print("Init AP with IP address: ");
  Serial.println(WiFi.softAPIP().toString());
  // WiFi.printDiag(Serial);
}

void initWifi()
{
  Config::load();
  ConnectWiFi_STA(true);
}

void disconnectWifi()
{
  Serial.println("Disconnectig WiFi...");
  WiFi.disconnect();
}
