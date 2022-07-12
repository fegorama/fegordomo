/**
 * @file 	UtilsConnection.h
 * @brief 	Utilidades de conexión (wifi, ethernet, etc.).
 * 
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef UTILSCONNECTION_H
#define UTILSCONNECTION_H

void ConnectWiFi_STA(bool);
void ConnectWiFi_AP(bool);
void initWifi();
void disconnectWifi();

#endif