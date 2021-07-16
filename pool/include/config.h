/**
 * @file 	config.h
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

const char *ssid = "FIDONET";
const char *password = "Manual_de_BASIC2";
const char *hostname = "ESP32_POOL";

IPAddress ip(192, 168, 1, 200);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);

#endif
