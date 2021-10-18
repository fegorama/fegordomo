/**
 * @file 	MqttClient.h
 * @brief 	Cliente de MQTT
 * 
 * @author 	Fernando González (Fegor)
 * @date 	15/10/2021
 * @version 1.0.0
 * 
 * Licensed under the EUPL-1.2-or-later
 */
#ifndef MQTTCLIENT_H
#define MQTTCLIENT_H

#include <WiFi.h>
#include <PubSubClient.h>

using namespace std;

class MqttClient {
    private:
        const char* mqtt_topic_receiver = "/casa/depuradora/rx";
        const char* mqtt_topic_publish = "/casa/depuradora/tx";
        const char* mqtt_server = "192.168.1.100"; // "broker.mqtt-dashboard.com";
        
        IPAddress mqtt_ip;

        const uint16_t mqtt_port = 1883;

        WiFiClient wifiClient;
        PubSubClient client;

        long lastMsg;
        char msg[50];
        int value;

    public:
        MqttClient();
        void init();
        void static callback(char* topic, byte* payload, unsigned int length);
        void reconnect();
        boolean isConnected();
        void loop();
};

#endif