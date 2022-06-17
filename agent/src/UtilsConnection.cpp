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

#include "UtilsConnection.h"

#ifndef CONFIG_H
#include "Config.h"
#endif

#define LOG_LOCAL_LEVEL ESP_LOG_INFO
#include "esp_log.h"

static const char* TAG = "connection";

// TODO Cambiar y usar los datos de configuración
const char *ssid = "SKYNET";
const char *password = "Manual_de_BASIC2";
const char *hostname = "ESP32_POOL";

IPAddress ip(192, 168, 2, 200);
IPAddress gateway(192, 168, 2, 1);
IPAddress subnet(255, 255, 255, 0);

/**
 * Conexión por Wifi en modo STA
 */
void ConnectWiFi_STA(bool useStaticIP = false)
{
  delay(10);
  //WiFi.mode(WIFI_STA);
  ESP_LOGI(TAG, "Connecting to wifi in STA mode...");
  WiFi.begin(ssid, password);

  if (useStaticIP)
    WiFi.config(ip, gateway, subnet);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
  }

  randomSeed(micros());
  ESP_LOGI(TAG, "Init STA: %s with IP address: %s", ssid, WiFi.localIP().toString());
}

/**
 * Conexión por Wifi en modo AP
 */
void ConnectWiFi_AP(bool useStaticIP = false)
{
  ESP_LOGI(TAG, "Connecting to wifi in AP mode...");
  WiFi.mode(WIFI_AP);

  while (!WiFi.softAP(ssid, password))
  {
    delay(500);
  }

  if (useStaticIP)
    WiFi.softAPConfig(ip, gateway, subnet);

  ESP_LOGI(TAG, "Init AP: %s with IP address: %s", ssid, WiFi.softAPIP().toString());
  // WiFi.printDiag(Serial);
}