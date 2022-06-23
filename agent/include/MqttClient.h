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
        const char* mqtt_server = "192.168.1.2"; // "broker.mqtt-dashboard.com";
        const char *ca_cert = "-----BEGIN CERTIFICATE-----\n\
MIIDmzCCAoOgAwIBAgIJANpg1/ojZmJkMA0GCSqGSIb3DQEBCwUAMGQxCzAJBgNV\n\
BAYTAkVTMQ4wDAYDVQQIDAVTcGFpbjEQMA4GA1UEBwwHR3JhbmFkYTEQMA4GA1UE\n\
CgwHR3JhbmFkYTELMAkGA1UECwwCQ0ExFDASBgNVBAMMCzE5Mi4xNjguMS4zMB4X\n\
DTIyMDYyMjIxMjMzN1oXDTMyMDYxOTIxMjMzN1owZDELMAkGA1UEBhMCRVMxDjAM\n\
BgNVBAgMBVNwYWluMRAwDgYDVQQHDAdHcmFuYWRhMRAwDgYDVQQKDAdHcmFuYWRh\n\
MQswCQYDVQQLDAJDQTEUMBIGA1UEAwwLMTkyLjE2OC4xLjMwggEiMA0GCSqGSIb3\n\
DQEBAQUAA4IBDwAwggEKAoIBAQC0u3phj2xj44Zsqp6THJ4acrj+5dUt/WCcXi7e\n\
wq/gHUvp2pPMTs+nzl5mB0U4Vt6e4m88tyP4OUh/RThRpQ3ynnxkXaaCPNCB/+nH\n\
NN9CtSzLmfIS5ezDm9m0ejpNTtKAXg/+HHzADZpP7n+neaaFu46AZUDg0PbNMlPc\n\
J4wosrGQhvLqWSA9LHJ45qOzQzcrSledaXGdZx+IbRp/M19q1Wxrxkr7sLZxtFDF\n\
/EpZFCwbnwiigqzEgBNHvvPR3MgB0jDoEmL79c3DzzxclriEWBHLZw+oJKReIR60\n\
t3Gsn/cCMRCX7vtUp9xkzT4QH7YjJgh9fqrrlqb26CTuWPwfAgMBAAGjUDBOMB0G\n\
A1UdDgQWBBQyMWmjf9xGYyGbdXeMsbFbqapy4jAfBgNVHSMEGDAWgBQyMWmjf9xG\n\
YyGbdXeMsbFbqapy4jAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQAP\n\
fNNW5N86u+m34iMEgcDoOTikYlvskaN1SZSxSMCfeXkVx94nYpk4UORlyHPXfS6Y\n\
MoKYpXWuYHb2AB4fZguRQRGW8lDsYQGcV2CzsoF702qG/9BAlMr+gLt48Or7+FXK\n\
JvJdQizNmEpZ5cNT1b4M6uzveOPpvMavvtV7kmFSHw1fIm+wq7ci3zg8axgzVJ5N\n\
pUSNIDeCOT+0aFmzKT9rKYuHbcXHNC9vDSdp0ll/6rHGzzUwE0pXFWJiveiXCOGq\n\
sFMGi9H0MwG2li6dU1RI8CP2muTJRYlqaJh3O5fufpIfCSH9sXmVmUHWZsVtgFDi\n\
ShynMkgqv0g/dt/W0Ih+\n\
-----END CERTIFICATE-----\n";
        const char *client_crt = "-----BEGIN CERTIFICATE-----\n\
MIIDSDCCAjACCQDLrSix16iPuDANBgkqhkiG9w0BAQsFADBkMQswCQYDVQQGEwJF\n\
UzEOMAwGA1UECAwFU3BhaW4xEDAOBgNVBAcMB0dyYW5hZGExEDAOBgNVBAoMB0dy\n\
YW5hZGExCzAJBgNVBAsMAkNBMRQwEgYDVQQDDAsxOTIuMTY4LjEuMzAeFw0yMjA2\n\
MjIyMTIzMzhaFw0zMjA2MTkyMTIzMzhaMGgxCzAJBgNVBAYTAkVTMQ4wDAYDVQQI\n\
DAVTcGFpbjEQMA4GA1UEBwwHR3JhbmFkYTEQMA4GA1UECgwHR3JhbmFkYTEPMA0G\n\
A1UECwwGQ2xpZW50MRQwEgYDVQQDDAsxOTIuMTY4LjEuMzCCASIwDQYJKoZIhvcN\n\
AQEBBQADggEPADCCAQoCggEBAL7mj50byeibgKu6yOQO72kNomffyYUPaoIBuXoH\n\
LXy08zvoy61QQCUy8rKPTcZx8L/3JAoy2d8HFx4ncBstK+FXT/gC9BDAYLYccM48\n\
qF9lMzNMN/DhZVnAa20MPqwMLo4idiRUCvZgGfN/d+yeYn0mQrbn2INzBB6VrN2H\n\
Yc1yAm6pENvTD7G66hDTMm0IL2xaU7bl0ezMyoRbtFn5jqJZYkEQyqEQOHAY/AfN\n\
Hn64kOQ5XBha/2uvT6gkgt0sUeTZ/BE5eZ/IOmW0F62955RvDamjym2VqjdcZ+uY\n\
YjXsIA/5M1bXwMCyL0DonxODJPp+Js2qSjo3gYnXtSKqb38CAwEAATANBgkqhkiG\n\
9w0BAQsFAAOCAQEAogU5bYVgxJrzs7Qm2kkufrEEK7KShIuZYmRl+D6Lropg8h/h\n\
BS5WH3/zwvdT/3NqWkH2heI2ps6rS5+uLTPNDESW/6pFfxo8SOewGrr9f7BsKST6\n\
ZrsqYzZDKs8InhRZfXtxlPrCufMkMDKOctRaLQO7CAXThiAE17wfTWhtkrPIqsuw\n\
Bz9uLvlzukvOquPxdTl8E4lIH6Dz1pjUqQ/k5TFK4kNvlD2FES9yRI8BcQWSWOh4\n\
/uCg4a1cOhu/C0E6tRN1UUl9K4273HUB3WVGYQ50EPI9KMml9olBePv3Ds/2sKus\n\
1IOs1b26RLkp2cwLC7mB4zfBOS7w6iBY2pqVBA==\n\
-----END CERTIFICATE-----\n"; 
        const char *client_key = "-----BEGIN PRIVATE KEY-----\n\
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+5o+dG8nom4Cr\n\
usjkDu9pDaJn38mFD2qCAbl6By18tPM76MutUEAlMvKyj03GcfC/9yQKMtnfBxce\n\
J3AbLSvhV0/4AvQQwGC2HHDOPKhfZTMzTDfw4WVZwGttDD6sDC6OInYkVAr2YBnz\n\
f3fsnmJ9JkK259iDcwQelazdh2HNcgJuqRDb0w+xuuoQ0zJtCC9sWlO25dHszMqE\n\
W7RZ+Y6iWWJBEMqhEDhwGPwHzR5+uJDkOVwYWv9rr0+oJILdLFHk2fwROXmfyDpl\n\
tBetveeUbw2po8ptlao3XGfrmGI17CAP+TNW18DAsi9A6J8TgyT6fibNqko6N4GJ\n\
17Uiqm9/AgMBAAECggEBAJwTyWs8IxJIwSSmrES5aUhM23Ft6Ecf2Ya9fjWk1K3X\n\
C975cD0dFbTUONWAkKgk1B8/InosJTFRjLccq22suumcB0fQsldX0jpSenBD661D\n\
Al94e2w3/DMrPSmFD2B+/Tk8N7dv9x6EZ/NLTnNo80E7HOSKHd0rFj0jRphAbdgX\n\
ahNzWVSNC0fH+UQ+cFRo7qdCTuwYaC5KbgOQJa8HMxsvNdgie1nJsXTyWvS0Vv2m\n\
lwQHLT4ZCK6fUeHWRsjBnoh/z7yi9iz9EzMhF+y83CJOX/WusFdcj57olGSrlgOf\n\
Dx5FVi9DRn1JNZS8yyp8jJyUVIaZK8jeajCrY5FDpPECgYEA7rsWmI/rJBjoDKhG\n\
VxH7zaNcDbgc4Wuc/xzEkH2gqeEGaZxknooDqkXDwIgYJq36XZpHZUt+o/NLRRdm\n\
qVtmz/GebxGtApsF5chA0RQ9W0xG8wYRUmOyHYFWQzj4RbtmL/59e9RslRghIlPi\n\
p/5aRHAtHNKl3CY+6NCL6xl3gbMCgYEAzLW8A2VIjiteniteO6um766/2Mi7oxBf\n\
RixOwGlcwNqATpQaEmdZGRuqjbCTqzMqhIkupQc27f7CbXVw4KZDCAgXLAvo/IcN\n\
HE3Gi+WDEQLR5L726mhUojkOkKY0FWlPB1RXLyZ8/h1RBlyNfZQ6uLBN9PRA5SEP\n\
o6Zqn529/QUCgYBRIyEA5RkkAZsMgjy15xTPxU/KEeMu8iY/xoVAHpJ2alaFmnPQ\n\
NlQckO2BCEA9FTBck8zkLo5yJoRUjSmFMd11m0cftMA6fHm6qR7ucJSR7RDIFC3a\n\
9Ktav9STsE8hVBndAf+gEIh++953Dk+gjSAc1otemnFoq08CTa7gTp2BZQKBgG+a\n\
MGslf53KdSYD/LbJSRRoj7zPCsTGezqVk0WA64lUVhkA0wnvf/qZQ61Fc+wk0+w3\n\
oc9klITBnBvyp9xBqE+roX5o69cohXRFzpWcRY0znVx448I9qM5cDt27Tq/Rnr0f\n\
NPzGneQn9XFjadvpwO3JaGa/9tUz5gPRCVYFuHtFAoGAHJoGpjOjaNB5xe53RLOd\n\
ovaI6+nOpcnXRKTmyD0qvvXjskGIvGFVnqvUwk8SBk7GpYJS1joduKALonWNsicE\n\
+CkUT/TQVgbmpRyE7H7s4e3x9sJFwsdDKvSMupbp1BqTO/yP+tPQtT1kredfU9HI\n\
57g0DRQ+/mWGNg+yjHwEkSo=\n\
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