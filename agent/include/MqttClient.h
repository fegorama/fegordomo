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
        const char* mqtt_server = "localhost"; // "broker.mqtt-dashboard.com";
        const char *ca_cert = "-----BEGIN CERTIFICATE-----\n\
MIIDpTCCAo2gAwIBAgIUWqXCopy4WXR4XVfVaT7qBWP+tQIwDQYJKoZIhvcNAQEL\n\
BQAwYjELMAkGA1UEBhMCRVMxDjAMBgNVBAgMBVNwYWluMRAwDgYDVQQHDAdHcmFu\n\
YWRhMRAwDgYDVQQKDAdHcmFuYWRhMQswCQYDVQQLDAJDQTESMBAGA1UEAwwJbG9j\n\
YWxob3N0MB4XDTIyMDUxOTE5MTQzNFoXDTMyMDUxNjE5MTQzNFowYjELMAkGA1UE\n\
BhMCRVMxDjAMBgNVBAgMBVNwYWluMRAwDgYDVQQHDAdHcmFuYWRhMRAwDgYDVQQK\n\
DAdHcmFuYWRhMQswCQYDVQQLDAJDQTESMBAGA1UEAwwJbG9jYWxob3N0MIIBIjAN\n\
BgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxNd55fOOqN5h1h4Wx/BMwY6ngP9E\n\
O47iSz7t7sm0pq+vubQr1hZtiYlFQYM7o/jY6naZq+mpqWPEWvz7QRYMFy0jaRrI\n\
r1P89rp9P0HZo6jH2lRQgjWadpiH3aMyGJoXUy2lw0NjRmgDkmushSOMuzDqgIr4\n\
40FNq7tSGMiSZlGWS9/rVVXmQEO1ezT/mTLlc/FqGv840wzAFyd9z/yWyA+tyK8f\n\
aqQRXoxVIgnfIsDHqkT1XD7ZBFkN4NQpyIHEQC9omVee1zF2oGlQ5Ws9tIOnsUqm\n\
7pcAxDTsjVhyqzCUEL+fFI4wefdKkh3+N+FYgOZqup3pMFpuakOPQtCk6QIDAQAB\n\
o1MwUTAdBgNVHQ4EFgQU+md9KhSMiaQPKXLtJDaatqGgRCYwHwYDVR0jBBgwFoAU\n\
+md9KhSMiaQPKXLtJDaatqGgRCYwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0B\n\
AQsFAAOCAQEAmR2pRtJw0SFaUViBUOtmL8jiNlT55l9GNtMLt7cLUazF3bgjd4h8\n\
KuwYKNDOzl+as0vI2I7LQbMEKyAKsKy6hpLP9dfymFLh26MfCDyNS4Q0MsBpWzW5\n\
PeNrDxXjM0uKy6Pt90D28nWHUXvLHWFjNsF9PVcG315GiSR8Em39GR6eLCQQZxO+\n\
fUtGb+sFMzeWSZXjtF6Ui+F4TTnIREDz+8wBeUzprpaKWid4KDmgJyG+meWdHesc\n\
ZMPpb5SFoJ882aLOn0WC6j7FlH69KDdDm20ECZ6eWQsdTg/DOgUNWvw16dp6b1Ei\n\
fF9anRcDgpEGKX05wpRNxbCiLVEj1/wGkA==\n\
-----END CERTIFICATE-----\n";
        const char *client_crt = "-----BEGIN CERTIFICATE-----\n\
MIIDTzCCAjcCFFvr4iqH14qSSpY0aVEUuT3XpN9yMA0GCSqGSIb3DQEBCwUAMGIx\n\
CzAJBgNVBAYTAkVTMQ4wDAYDVQQIDAVTcGFpbjEQMA4GA1UEBwwHR3JhbmFkYTEQ\n\
MA4GA1UECgwHR3JhbmFkYTELMAkGA1UECwwCQ0ExEjAQBgNVBAMMCWxvY2FsaG9z\n\
dDAeFw0yMjA1MTkxOTE0MzdaFw0zMjA1MTYxOTE0MzdaMGYxCzAJBgNVBAYTAkVT\n\
MQ4wDAYDVQQIDAVTcGFpbjEQMA4GA1UEBwwHR3JhbmFkYTEQMA4GA1UECgwHR3Jh\n\
bmFkYTEPMA0GA1UECwwGQ2xpZW50MRIwEAYDVQQDDAlsb2NhbGhvc3QwggEiMA0G\n\
CSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCZOt9TXY+QoknmSqSv9GCU2zj1Ibp5\n\
bhH/7lgYrrpkAaBvfzs7en0fdiLJ/MTE6VPz78qluqn7F2M8A6uiS7np89h3Mv3I\n\
uFMungYL+wi9uo+rix34oFHcYxwt8U9KhYo82od4em72IbnJM4bySTMhOH5MYJrV\n\
acydht4Zi/IgHwxGQUMcEtxqJNyiZwpEfujEfPnKXx948FVJv8PQ8cL/5No9u7Jm\n\
xQaUV+sMVyJ0mHgmChrW+foABEVN4ZYYceAleNfwj4VBv61VQ9d4FjhnsoPVnur1\n\
pVX0fXNjsHTgdgGjWT0c68J2MWqD0CbypaiXBaeMOnaaVDI9LymfDSjhAgMBAAEw\n\
DQYJKoZIhvcNAQELBQADggEBAJptKyT5qMza+qQmTg/kOtlSBOE2Zum/lM1wmi98\n\
PjLvBbOreL1MCFJ/pYKYBA10m31aBXRXe6nBvtXgYL8jvZ7tXBeYbtlv/8p3gEgG\n\
a9pE2J0doPPuGAREM5O4/amHt0hErq2zxZJ/cLemyCqeXRWoGhTVurtYtvQ3ehd9\n\
F1TzrB1GPuA6jtSetomccySMdM0lmWa090aoP8hfnZsNQJTt/O9uIbO6YpsjZaGK\n\
YmJZk3rV6HYTaLJnysaDRfF+0CuZQMf/+zQyiR7OxUMhEizbE5u7UnjQZn4zfRiC\n\
k3jpdmY7fDq37sqY6l5iLAzfbFvRf+GvI3e92ai6APkkWIU=\n\
-----END CERTIFICATE-----\n"; 
        const char *client_key = "-----BEGIN PRIVATE KEY-----\n\
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZOt9TXY+Qoknm\n\
SqSv9GCU2zj1Ibp5bhH/7lgYrrpkAaBvfzs7en0fdiLJ/MTE6VPz78qluqn7F2M8\n\
A6uiS7np89h3Mv3IuFMungYL+wi9uo+rix34oFHcYxwt8U9KhYo82od4em72IbnJ\n\
M4bySTMhOH5MYJrVacydht4Zi/IgHwxGQUMcEtxqJNyiZwpEfujEfPnKXx948FVJ\n\
v8PQ8cL/5No9u7JmxQaUV+sMVyJ0mHgmChrW+foABEVN4ZYYceAleNfwj4VBv61V\n\
Q9d4FjhnsoPVnur1pVX0fXNjsHTgdgGjWT0c68J2MWqD0CbypaiXBaeMOnaaVDI9\n\
LymfDSjhAgMBAAECggEAC9qGjo6VKGqtjdT7aCiJqipKqIfwmCvgKkIiwDIQIDln\n\
UwjEKRtA1QS7YeXIcQqIdef4b26rxKKwCLiooo2kzF49Qidd2Opiy58/jNYMJzqT\n\
RQF7kua6vnJPqpbZe+8tslHIxhSumHgjxQFT8Kf3U8a+L866BkVre0ovd0l2CcRO\n\
XFpJ/JEZeSEEOF2RHH9YmYNed/HDaFO4U7eJHtM65YxihVa4uwtrKPAwalMop7m+\n\
7pjxc+23XakWBx8vm89wJKLBcX+FcKjZFb4ZQiHeGm8q+5lmZ/ENAHkwylcmjce1\n\
hjfz4tot0m5HhSgVZXDJHEKzelFecJAR3/JBocH2YwKBgQC2acWV4tCin1VhI03n\n\
eOQ/+fri7+5WUML+uST22uVCN+PlkdHZJdipinX/DiqCRNFnFXwMhp4OCTDFkNnK\n\
ts8Xsm5ABVjussq3kphPZg4kNQOXpc/+Bkm5TUs7vO0mcnKZiB5/iw94ohqgtl1D\n\
0lzIuGTg/Q9PoAzDQRAsNd6gZwKBgQDXC0jAnM4Euj+sK8Rwx/70L9PVMbv63U3D\n\
y+8+lvuxpWi151eLhTOVmZb8kj8XSvLjXc0lhdDpYNnK+19MdGAftYGJyu6aQJMQ\n\
RiYswX6WLE6olgYmUmWnom54NRgCHEQO6TlWlCeb/qamfBRkW5kxNuCMAE9PcblR\n\
tsKHsKb/dwKBgQCHKPhuhxFwbf8larNewATgIqnzXTSMYGsbsrPg9XDKD/+dKt8C\n\
tzhC54Vm52g3/iXq69But5CFdendeebmfW4VcPCgH1Y2B+SNAOI6crQnv+BZjVMA\n\
NkiKNOt56BMQWdzwRhvTBYcBK5vvWTDiAA0f7QFSdC/UIBNU2fDR0ViUIQKBgFo2\n\
NKYA4fhfvbhXgAgkDlOYANCuFYLu9eGOmuXx5tKmZl/xFevEzFgU359DJs55tZ0m\n\
S5pTIcnVxGf/vgHPzEdhGF+azy/MqSUpmHgsEBOlsLXOgfPvmw+Q97Db7R9thiuP\n\
MWudqWQqlvS+lrtnJcPclqSEuMDGH40lE0RjGhuPAoGBAJsRsqgWmxcN0K2VrvYP\n\
cogZggoPgdst9YK/IBvByaSRyThWQeip2IAFmo4etigNBxEInNZ3KAY+NpvTeDre\n\
pU/wXBR+tfc/7saUYvmluepwF/1HuDl0oXTccR3D1tX2y+F3zdvVOmpbESPvWMY9\n\
TFo2izcH5abcT3CzkhDt8hTe\n\
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