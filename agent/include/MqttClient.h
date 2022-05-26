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

#include <WiFiClientSecure.h>
// #include <WiFi.h>
#include <PubSubClient.h>   

using namespace std;

class MqttClient {
    private:
        const char* mqtt_topic_receiver = "pool_inbound";
        const char* mqtt_topic_publish = "pool_outbound";
        const char* mqtt_server = "192.168.2.2"; // "broker.mqtt-dashboard.com";
        const char *ca_cert = "-----BEGIN CERTIFICATE-----\n\
MIIDqTCCApGgAwIBAgIUBV4Jz5RU0necmNVNDMFAf2jmC9MwDQYJKoZIhvcNAQEL\n\
BQAwZDELMAkGA1UEBhMCRVMxDjAMBgNVBAgMBVNwYWluMRAwDgYDVQQHDAdHcmFu\n\
YWRhMRAwDgYDVQQKDAdHcmFuYWRhMQswCQYDVQQLDAJDQTEUMBIGA1UEAwwLMTky\n\
LjE2OC4yLjIwHhcNMjIwNTI2MTgwODAyWhcNMzIwNTIzMTgwODAyWjBkMQswCQYD\n\
VQQGEwJFUzEOMAwGA1UECAwFU3BhaW4xEDAOBgNVBAcMB0dyYW5hZGExEDAOBgNV\n\
BAoMB0dyYW5hZGExCzAJBgNVBAsMAkNBMRQwEgYDVQQDDAsxOTIuMTY4LjIuMjCC\n\
ASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAIqDwP9nH6pkq3ho7CA5mMqV\n\
/pkdLxcLZeDyV0mBc7DyD7UarjjH/ONPql1U4oCBYVS9Z4yyFSTSnHvotFNY+oaG\n\
0z3s1Vd0KwgdVJkN04Y6GjkKWZOXroFk6RD1vtsPaH3p0zHWsxJT+FefoFaA+qvQ\n\
EOIjfuYluEFBWDtJ8emC8DYbu9fHROxDJACnNoEgH639v7LvnoTW/4YlTG81ZvY8\n\
0O9abhL1+/YejbSmFKGoQO4qmHAbyIPTG7OaeZWsPRirvVx47CUBqlgZBQ8xPJOa\n\
yxBoUDrmqf7vhEDcJnlXoh5tGGUNAlJzgpLG/7KEhCZCInZSjq1EZtmRoe2bN80C\n\
AwEAAaNTMFEwHQYDVR0OBBYEFBCSCaSHSB/4lPO0dFE9YWIZbhYkMB8GA1UdIwQY\n\
MBaAFBCSCaSHSB/4lPO0dFE9YWIZbhYkMA8GA1UdEwEB/wQFMAMBAf8wDQYJKoZI\n\
hvcNAQELBQADggEBAC9wkq5xIMhW5hWlqAlNk4Qtko9DhaZwTMfvGKEs1xVLc2Rc\n\
mTyvr7NMb3qUpmXRQz/XgljZdleFSGm6GkgmfsWD39sRseedQmSOtLvr15hRqsx3\n\
8vszoyghBhT9yLQt9R9L8hZty3UXtcQWosr8zpSYokXTkQYTx+jhsJdptFZAG/t8\n\
Zvam/ov9H+VrmK9VPE4RsYla+uO2cVeHkxayVf2VnuTZh/xzIMYIeQigiycrUR7K\n\
B73PFkPOJfkmgbN3pZxOMqS4LZ3RP8w4NuyLfrGPnDKAsm3jXBby5dErjL6CoFuq\n\
j/kN6kJrI//PHYUZm8l5ZNzDoBwmvUU3Xxxqnxs=\n\
-----END CERTIFICATE-----\n";
        const char *client_crt = "-----BEGIN CERTIFICATE-----\n\
MIIDUzCCAjsCFB19ZtrC0XGvU+Vk0o+RSaWgHzhfMA0GCSqGSIb3DQEBCwUAMGQx\n\
CzAJBgNVBAYTAkVTMQ4wDAYDVQQIDAVTcGFpbjEQMA4GA1UEBwwHR3JhbmFkYTEQ\n\
MA4GA1UECgwHR3JhbmFkYTELMAkGA1UECwwCQ0ExFDASBgNVBAMMCzE5Mi4xNjgu\n\
Mi4yMB4XDTIyMDUyNjE4MDgwNVoXDTMyMDUyMzE4MDgwNVowaDELMAkGA1UEBhMC\n\
RVMxDjAMBgNVBAgMBVNwYWluMRAwDgYDVQQHDAdHcmFuYWRhMRAwDgYDVQQKDAdH\n\
cmFuYWRhMQ8wDQYDVQQLDAZDbGllbnQxFDASBgNVBAMMCzE5Mi4xNjguMi4yMIIB\n\
IjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsctGNMQFyzwNsJoqZ4BsBW0u\n\
gaYOTRTWRk4yyHdcWcZKg/MGtkkhhC7u6ZJGm+d7xw3gTEhAAU9D46pIFqvdRe6N\n\
GO6JvXa4zYywKNU0wjtk+eY/CXlOMP4+k6Tdbjsfa+F0q+HIrM8BlgjAsycuXN24\n\
ZHsK68U8D4trfexA9hg98poaZAJ/XEwubqpLjJ90SONE5odPYFUZyhGuNOpu0jO5\n\
3AIZ3d8ZkqeW6wvdj/Owi70Q3Kz5mkgqfOBtzWK3dh2YeeIz67mD6DPWow5aRi3+\n\
iuwBbWcHVvJMJ34JQBqJUEDbkMkxM9S+F07eUE+u4C8MZcrmCXU5ItJyBFnoEwID\n\
AQABMA0GCSqGSIb3DQEBCwUAA4IBAQACzLedJ+Ipx67XEmA0QEc95v+cBiKW5EoS\n\
jgV7c/JZGrCtSeptRzb1DYGgsYgFyyVymKHBkpxdSQXZDGHxYsYck3P1fn7oroaN\n\
BQbbSfZjOvzbqw4UqD+qrt3S0F4mmfLbdmo4p+5ImEvxZCf/W41DTC+8tjpn5dYK\n\
hdvI87Mn7ccbfvfqhNTbzeJBF+rDMuqww3vcdAsgXAvJiihvOhK3LoCMdjAfJW+V\n\
ONTNnUwPHe4JMFObr3gGiVf6yYJUhWdf/HbBg3ADb2rv9KsoTDiLwjBDw9Tbd2mD\n\
QBadHtFcKErtt2B4/h78vK7jpeBvFmISJYsleUF6EduIixSQVKRC\n\
-----END CERTIFICATE-----\n"; 
        const char *client_key = "-----BEGIN PRIVATE KEY-----\n\
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCxy0Y0xAXLPA2w\n\
mipngGwFbS6Bpg5NFNZGTjLId1xZxkqD8wa2SSGELu7pkkab53vHDeBMSEABT0Pj\n\
qkgWq91F7o0Y7om9drjNjLAo1TTCO2T55j8JeU4w/j6TpN1uOx9r4XSr4ciszwGW\n\
CMCzJy5c3bhkewrrxTwPi2t97ED2GD3ymhpkAn9cTC5uqkuMn3RI40Tmh09gVRnK\n\
Ea406m7SM7ncAhnd3xmSp5brC92P87CLvRDcrPmaSCp84G3NYrd2HZh54jPruYPo\n\
M9ajDlpGLf6K7AFtZwdW8kwnfglAGolQQNuQyTEz1L4XTt5QT67gLwxlyuYJdTki\n\
0nIEWegTAgMBAAECggEACrTVePb27Fqk7mYSOKJcnuUIwGjqAo1HK/uohjbd2lAd\n\
C54WYOK9gHK9E1aqJthFNWLqB13wcMO/sFWAOtFFpWDsoTw4Ei80iLg4oXTZVPrz\n\
pTLOx/jEkyG0TfrHFMy9oybz+ifCCFn82/aR+BfbbF8zq+VW7oS1jbAVHIOeE9Of\n\
LkHpBd789j4PrWkLmcANdcrgvC1CkDN16sf7KAGyiVaongjxSTfBFimzyGJehdTa\n\
K1iOGzr8sXTBkuRkaO4U9VRap6aFfp78XqteIWnZj1E52M+uEqpb1IkXUErGwn61\n\
exC6Mxrchi4iPsWECF3GHYOoBHEsBTza+vLPJDCAXQKBgQDOr3Wqs5jo18we1s+j\n\
UyxVZtWeA3m2bwIX7kjMu5jyLwEedASGjal0AUdoP8NEPMB0TqrOzbiPloD0golM\n\
NkG/u1o4JDYAk+XOH2qzf0Rf9S4XBEUaUzlnaL9TWDUSlm1SuMysPiyE9xBbKUqP\n\
buCrXaoZx71LJmNqFgDZLpj8hQKBgQDcNxp+TGsXO32A+8z4J5GYQqbVwA6vQnjZ\n\
j2CT5J5qw55rfSp5tnAz0/GN207lqUFPwDgUNf+9DIngaGxCKlip7TW9KO9cjqQx\n\
9ploXiN/L9VwGjELuuRjtS+C41LBUvNef/VRrlSxnmH0ZsiLmrVEOqDfBV/9Q7r4\n\
KAtbFMNhtwKBgCSxi89RcT0tSqTS+CwD0SQcTKJv9YTI8nMzL6OBTqb4Z1dzu3Xz\n\
4nig3X+Jim4r5vL/IF30seZ0DtLZ9ewc2dFjPqIm85topmzQsQ3K82Whzpvxex8w\n\
VUmhlA4E4+8325LLEjMmGptEjZfRndYXFjX2PAO1rUqRNycXXvEvz6NNAoGBAMG0\n\
9ukB73GQMuWymKnwpHF6r6Z2OnSP1B4LsLhm8bKYQFHs1+AYSrpMRL6zmsMKDxlH\n\
zAx7M2noDaAIOrI47aULs6mgyGxyTJxmfqr/o9nNCcKCR4I584yBZDmOHM1RWiJW\n\
DES9RGK5HxENGHs0qx5JNBj5cWYmneCL2PFVYkghAoGAXQNS/YRrEfufcMVXsOGX\n\
AvSXNrH+En/igRL3oLkDTYBZm2LoiWoUQQWB/CT2OJPnK6askfFnYc3S9uuKjr0H\n\
M+8widnK3ZvRue6JhS1hYIIyA7fF1QozeRTW3hmTK/lw+7rCntQ95mvwr1DEJKU2\n\
dD8L1sxesmcrVDj6dy76f3I=\n\
-----END PRIVATE KEY-----\n";
        
        IPAddress mqtt_ip;

        const uint16_t mqtt_port = 8883;

        // WiFiClient wifiClient;
        WiFiClientSecure wifiClient;
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