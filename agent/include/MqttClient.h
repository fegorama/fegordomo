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
MIIDqTCCApGgAwIBAgIUYTNML1ZvjF4ygV6b2cBMyRw8smQwDQYJKoZIhvcNAQEL\n\
BQAwZDELMAkGA1UEBhMCRVMxDjAMBgNVBAgMBVNwYWluMRAwDgYDVQQHDAdHcmFu\n\
YWRhMRAwDgYDVQQKDAdHcmFuYWRhMQswCQYDVQQLDAJDQTEUMBIGA1UEAwwLMTky\n\
LjE2OC4xLjIwHhcNMjIwNjE5MTEwOTA3WhcNMzIwNjE2MTEwOTA3WjBkMQswCQYD\n\
VQQGEwJFUzEOMAwGA1UECAwFU3BhaW4xEDAOBgNVBAcMB0dyYW5hZGExEDAOBgNV\n\
BAoMB0dyYW5hZGExCzAJBgNVBAsMAkNBMRQwEgYDVQQDDAsxOTIuMTY4LjEuMjCC\n\
ASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALbfdynrS9UpU3Jnj/Qb2jmQ\n\
g9h3WGI0suoEJy8DnHm6EP4fGMyw2egUNNIxWuoCFuWfF1OrUWcv1iAUZpFqg54X\n\
sEiJ6QkfFCYJaH2m1VIWQpzWn8ZrxwfXIgvdjuAv//AbDeCJ3z8csXim730sNceg\n\
jwT6R/oB2lBmqpaf8D9X/+98GltltSeW0byAdcluuEbQ7aKoeux6Js7i3Wy55TjC\n\
1s6uOq036ovbTymaAqc7WCefF7IQGheN80pNPhgvGiOBqKoaLdfig8DRj0OqC8aP\n\
WSuN7Pzn6fHoPJBGyDFVC0pGeNh6kwyratZMrS3fxSdbu0xil1RMK5gqiKlf3G8C\n\
AwEAAaNTMFEwHQYDVR0OBBYEFGShVgklz+4iR41lEY5DUGsS2wmkMB8GA1UdIwQY\n\
MBaAFGShVgklz+4iR41lEY5DUGsS2wmkMA8GA1UdEwEB/wQFMAMBAf8wDQYJKoZI\n\
hvcNAQELBQADggEBAEPsyk+n7r13k+C3AOMgNhSL3ACDzOgq1aN96Gohm4KN3XOe\n\
IR4O2f3EME/uqAVEwljaEezSnQHFI+PzMfFq0Rn2CVVbbITQaNwWgE1QeGoBZnJ8\n\
28FhvIXbo/2yrWMHlo2oGuJC6PRCm/7sIS0mrGJkNtTRhdkwDtIWuVcYSS/++JDm\n\
waonTtZBQ2gc3xsrPhpyan61eklsKFPMDuJPbEG/62xL4Sse15drsSEyPNRHFsQG\n\
oRzBMmdUJUx3xM01XAFrmCeU5RkdRE8V2hNCGqmmJZL3UkMnwEqMa+wKIQBUAnRu\n\
2bemgbfSxlvEC2GH5ILSIUXANcZ+epLxiR+Dmcs=\n\
-----END CERTIFICATE-----\n";
        const char *client_crt = "-----BEGIN CERTIFICATE-----\n\
MIIDUzCCAjsCFCut9wM2/l3bs/ko3KKZ3+uvYyhBMA0GCSqGSIb3DQEBCwUAMGQx\n\
CzAJBgNVBAYTAkVTMQ4wDAYDVQQIDAVTcGFpbjEQMA4GA1UEBwwHR3JhbmFkYTEQ\n\
MA4GA1UECgwHR3JhbmFkYTELMAkGA1UECwwCQ0ExFDASBgNVBAMMCzE5Mi4xNjgu\n\
MS4yMB4XDTIyMDYxOTExMDkxNFoXDTMyMDYxNjExMDkxNFowaDELMAkGA1UEBhMC\n\
RVMxDjAMBgNVBAgMBVNwYWluMRAwDgYDVQQHDAdHcmFuYWRhMRAwDgYDVQQKDAdH\n\
cmFuYWRhMQ8wDQYDVQQLDAZDbGllbnQxFDASBgNVBAMMCzE5Mi4xNjguMS4yMIIB\n\
IjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwTb0dSvzwXwcf47aX6BFHZZa\n\
0+w704KQJ9GwyRELJYAeM9gZNRl8Gru9Eo9m+TRNg0/Isys3uU4QXSNA5ajMOcnT\n\
SO8J08IY8C57nxNrlV7NaDt8CgXePBaUyTWkzojddAA54nNdd7YneZl4d0LYn4gw\n\
sm1TZQT7ucuv7bygoy15ClLO5RfZCNt96Fjb2z5pb3NTVxCnVppnUx10AC52s9uL\n\
BjckIDjz0NGW6dYjz50/TB5AQXLJAs0Jt6A0KgObQjr7JD6hO6UaWJ+a+gxsp0HY\n\
rYm3nPZ+YVtqQhaJM/lWH9Ijd+lUeGKeFbcNlCfju+xPWOR6dwlUOVFsc940nQID\n\
AQABMA0GCSqGSIb3DQEBCwUAA4IBAQBz9QHLbNF0pZZZzk3upEiYAnBsKR6TAiKL\n\
DH8gxDwqBkul1Gl39Ly1HM9DfrdLIwxEgqsEtZEbIlf7QHZtCqEBM9eVi9kb/FfM\n\
iV3Iqx7nkFOxe8G2E7Fm2U/gfSXnE92l2EZO6MrqnQm0DiZBehDT+a3PzbO331QP\n\
cG5SnRCIO9Joj8NfdRFflLeh2nvDXeiu8jlJnc9JXf5GSKG5xM7V1mTYJ1p8yGoX\n\
7lenGuxUYnnvv61asjGjInpk/3KC/qRWkqe7nl/Oy7NsSzbiX7K/AdVk8gLIKqLS\n\
mE20LQnPEKG7p8m9j7c0Pp6iLim4wu6/JHe5DekFAdDSWKFvhr1g\n\
-----END CERTIFICATE-----\n"; 
        const char *client_key = "-----BEGIN PRIVATE KEY-----\n\
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDBNvR1K/PBfBx/\n\
jtpfoEUdllrT7DvTgpAn0bDJEQslgB4z2Bk1GXwau70Sj2b5NE2DT8izKze5ThBd\n\
I0DlqMw5ydNI7wnTwhjwLnufE2uVXs1oO3wKBd48FpTJNaTOiN10ADnic113tid5\n\
mXh3QtifiDCybVNlBPu5y6/tvKCjLXkKUs7lF9kI233oWNvbPmlvc1NXEKdWmmdT\n\
HXQALnaz24sGNyQgOPPQ0Zbp1iPPnT9MHkBBcskCzQm3oDQqA5tCOvskPqE7pRpY\n\
n5r6DGynQditibec9n5hW2pCFokz+VYf0iN36VR4Yp4Vtw2UJ+O77E9Y5Hp3CVQ5\n\
UWxz3jSdAgMBAAECggEAJzjIaaO9NPUElPlTB4/xj9bGg9gj8eUCbe7heAODc36d\n\
QvF2doLAoG2lbUfag7KUHJic2ZAnnPCFnl6KbUR1hrKbdyX9JPU1GkaZVjq7P2Ry\n\
Bptyq9cynJiJ5gmDdeCvMb3dAkTgf61kuIjIvU7+ye270vCh+fMHxZjdUwGUS2NU\n\
hB71msgf8lmwoh+QuC29Xl4Po0KTzoxGPQoYCXT5IXWY1fQEljil5ROSX6COAgl8\n\
aa4Jha/fClB+EmJYr82bgwyzY9BUQger5+OzHSe7xRTHRj/jaJhpyhaZUOOz1sSm\n\
mc2QRyQRT9QI9U0O/Y+DNHaqfTYqD2EmRWO+QL6egQKBgQDsi/QRtzcMBKeWpc+X\n\
XcaT/+42pMjxErCQGn3yiJlldTvGMr+9RvkK2MM2PuhHmu9Uve3hzfOwUB88s7bk\n\
mF+ZCC34Rl7oVk5321FF+ry+3UwUsgyBA6xUkisQKAufZ9oP8YSgxHScmOUKFC6t\n\
1pJajizx2IX1d5GmMIbN6b0MHQKBgQDRGroguMqiOJzGv4cyeWifGNU+TqWdcw06\n\
MduQxhF4PhEFV00JxtgkPeSy6rDx4Efwxvmf2RNt4b24t3O8kPdgXSLC7EXZ+gy6\n\
xcgUmruMz3yPAHv5UODIbUUPFtB2yMnfbjqJI3h7KhBRQtPhq3UbV4QqfwihHFPX\n\
m4CYP1higQKBgQDDpxLuoEtwrlzr1yP4gEEi4zpmijAGbj4KQ09PjzrJ14jka896\n\
QDz3gskckjXQHE+/GQWskovwlqTvsXoiZAIWctSLGbGgnNp0erwjlLwPZr1qFMcQ\n\
4zfkD658w5Kxe9W7ADqCO1x0tmYjywO9F9CgBB8avsw2d3rKKjMmGCIc5QKBgHMZ\n\
TR652QAWPWXBA0ArSAVa+/YCAHN8YumHjB5SiEcxktjnxwX8p/i7AOni+WcKV08X\n\
l0v/hwxS18WXZWOEQEaQvZOhzok/dEZSm+GLknTduOhWEeeYN0Lbssmtey4HM+eC\n\
m0Hlfmn9K2IvcMWlRR65u4xQxIU6PIRPmiczpwkBAoGANz+xegsUl/eE01VejwC2\n\
wENhTa43Qt8fDQAE2X5aqJcx1jIxNVXOLl58dF53UD2GVBOumqgWul5VB0MEMFOR\n\
m5QinVvgUiN3ZNpHAiPP53WBMkSrZNoR2wKcXIaOdd8m0e3RgnMy0gU4Qua3j9iF\n\
ZOEP8w5TA8iTH3qn+RQWRUw=\n\
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