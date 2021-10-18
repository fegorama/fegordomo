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

const char *ssid = "FIDONET";
const char *password = "Manual_de_BASIC2";
const char *hostname = "ESP32_POOL";

IPAddress ip(192, 168, 1, 200);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);

/**
 * Conexión por Wifi en modo STA
 */
void ConnectWiFi_STA(bool useStaticIP = false)
{
  delay(10);
  //WiFi.mode(WIFI_STA);
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);

  if (useStaticIP)
    WiFi.config(ip, gateway, subnet);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print('.');
  }

  randomSeed(micros());
  Serial.println("");
  Serial.print("Iniciado STA:\t");
  Serial.println(ssid);
  Serial.print("IP address:\t");
  Serial.println(WiFi.localIP());
  // WiFi.printDiag(Serial);
}

/**
 * Conexión por Wifi en modo AP
 */
void ConnectWiFi_AP(bool useStaticIP = false)
{
  Serial.println("");
  WiFi.mode(WIFI_AP);

  while (!WiFi.softAP(ssid, password))
  {
    Serial.println(".");
    delay(1000);
  }

  if (useStaticIP)
    WiFi.softAPConfig(ip, gateway, subnet);

  Serial.println("");
  Serial.print("Iniciado AP:\t");
  Serial.println(ssid);
  Serial.print("IP address:\t");
  Serial.println(WiFi.softAPIP());
  // WiFi.printDiag(Serial);
}