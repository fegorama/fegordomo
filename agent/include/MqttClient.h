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
        const char* mqtt_topic_receiver = "pool_inbound";
        const char* mqtt_topic_publish = "pool_outbound";
        const char* mqtt_server = "192.168.2.2"; // "broker.mqtt-dashboard.com";
        
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
        void pub(const char *msg);
};

#endif