/**
 * @file 	APIRest.cpp
 * @brief 	API de llamadas a RESTful
 *
 * @author 	Fernando González (Fegor)
 * @date 	12/07/2021
 * @version 1.0.0
 *
 * Licensed under the EUPL-1.2-or-later
 */
#include <ESPAsyncWebServer.h>
#include <ArduinoJson.h>
#include <Stream.h>
#include "esp_log.h"
#include "APIRest.h"
#include "Config.h"
#include "NetConnection.h"

char buffer_page[4096];

const char head_page[] PROGMEM = R"rawliteral(
<!DOCTYPE html>
    <html>
        <head>
        <meta charset="UTF-8">
        <title>Fegordomo</title>
        <style type="text/css">
			/*
			* {
				margin:0px;
				padding:0px;
			}
			*/
			#header {
				margin:auto;
				width:500px;
				font-family:Arial, Helvetica, sans-serif;
			}
			
			ul, ol {
				list-style:none;
			}
			
			.nav > li {
				float:left;
			}
			
			.nav li a {
				background-color:#00a;
				color:#fff;
				text-decoration:none;
				padding:10px 12px;
				display:block;
			}
			
			.nav li a:hover {
				background-color:#00f;
			}
			
			.nav li ul {
				display:none;
				position:absolute;
				min-width:140px;
			}
			
			.nav li:hover > ul {
				display:block;
			}
			
			.nav li ul li {
				position:relative;
			}
			
			.nav li ul li ul {
				right:-140px;
				top:0px;
			}

			.box { 
                font-family: sans-serif; 
                font-size: 18px; 
                font-weight: 400; 
                color: #ffffff; 
                background:#099 
            }
		</style>
        </head>
)rawliteral";

const char begin_body[] PROGMEM = R"rawliteral(
<body>
)rawliteral";

const char menu_page[] PROGMEM = R"rawliteral(
    <div id="header">
			<ul class="nav">
				<li><a href="main">Inicio</a></li>
				<li><a href="">Configuración</a>
					<ul>
						<li><a href="connection">Conexión</a></li>
                        <li><a href="mqtt">MQTT</a></li>
						<li><a href="message-manager">Gestor de mensajes</a></li>
						<li><a href="certs">Certificados</a></li>
					</ul>
				</li>
				<li><a href="">Acerca de</a></li>
			</ul>
		</div>
)rawliteral";

const char end_page[] PROGMEM = R"rawliteral(
    <body><html>
)rawliteral";

const char main_page[] PROGMEM = R"rawliteral(
<div id="main_page" class="box">
  <form>
  <br><p></p>
  <label for="wifiSSID">SSID: </label>
  <label>%wifiSSID_value%</label><br>
  <label for="username">Host name: </label>
  <label>%hostname_value%</label><br>
  <label for="password">Password: </label>
  <label>%password_value%</label><br>
  <label for="ipaddress">IP: </label>
  <label>%ipaddress_value%</label><br>
  <label for="ipgateway">IP Gateway: </label>
  <label>%ipgateway_value%</label><br>
  <label for="ipmask">IP Mask: </label>
  <label>%ipmask_value%</label>
  </form>
<div>
)rawliteral";

const char connection_config[] PROGMEM = R"rawliteral(
<div id="form_config" class="box">
<br><br><p></p>
<form action="saveConnection" method="post">
  <label for="wifiSSID">SSID: </label><br>
  <input type="text" id="wifiSSID" name="wifiSSID" value="%wifiSSID_value%"><br>
  <label for="username">Host name: </label><br>
  <input type="text" id="hostname" name="hostname" value="%hostname_value%"><br>
  <label for="password">Password: </label><br>
  <input type="password" id="password" name="password" value="%password_value%"><br>
  <label for="ipaddress">IP: </label><br>
  <input type="text" id="ipaddress" name="ipaddress" value="%ipaddress_value%"><br>
  <label for="ipgateway">IP Gateway: </label><br>
  <input type="text" id="ipgateway" name="ipgateway" value="%ipgateway_value%"><br>
  <label for="ipmask">IP Mask: </label><br>
  <input type="text" id="ipmask" name="ipmask" value="%ipmask_value%"><br>
  <button type="submit">Guardar</button>
</form>
<div>
)rawliteral";

const char message_manager_form_begin[] PROGMEM = R"rawliteral(
<div id="form_config" class="box">
<br><br><p></p>
<form action="saveMessageManager" method="post">
  <label for="wifiSSID">SSID: </label><br>
  <input type="text" id="wifiSSID" name="wifiSSID" value="%wifiSSID_value%"><br>
  <label for="username">Host name: </label><br>
  <input type="text" id="hostname" name="hostname" value="%hostname_value%"><br>
  <label for="password">Password: </label><br>
  <input type="password" id="password" name="password" value="%password_value%"><br>
  <label for="ipaddress">IP: </label><br>
  <input type="text" id="ipaddress" name="ipaddress" value="%ipaddress_value%"><br>
  <label for="ipgateway">IP Gateway: </label><br>
  <input type="text" id="ipgateway" name="ipgateway" value="%ipgateway_value%"><br>
  <label for="ipmask">IP Mask: </label><br>
  <input type="text" id="ipmask" name="ipmask" value="%ipmask_value%"><br>
  <button type="submit">Guardar</button>
</form>
<div>
)rawliteral";

String processor(const String &var)
{
    if (var == "wifiSSID_value")
        return Config::wifiSSID;
    else if (var == "hostname_value")
        return Config::wifiHostname;
    else if (var == "password_value")
        return Config::wifiPassword;
    else if (var == "ipaddress_value")
        return Config::staticIP;
    else if (var == "ipgateway_value")
        return Config::staticGW;
    else if (var == "ipmask_value")
        return Config::staticSN;
    return String();
}

/**
 * Métodos privados
 */

int APIRest::getIdFromURL(AsyncWebServerRequest *request, String root)
{
    String string_id = request->url();
    string_id.replace(root, "");
    int id = string_id.toInt();
    return id;
}

String APIRest::getBodyContent(uint8_t *data, size_t len)
{
    String content = "";

    for (size_t i = 0; i < len; i++)
    {
        content.concat((char)data[i]);
    }

    return content;
}

/**
 * Métodos públicos
 */

APIRest::APIRest() {}

void APIRest::notFound(AsyncWebServerRequest *request)
{
    request->send(404, "application/json", "{\"message\" : \"Service not found!\"");
}

void APIRest::health(AsyncWebServerRequest *request)
{
    Serial.println("/health");
    String res = Config::load();

    if (res != NULL)
    {
        request->send(200, "application/json", "{\"health\" : \"OK\", \"config\" : " + res + "}");
    }
    else
    {
        request->send(200, "application/json", "{\"health\" : \"KO\"}");
    }
}

void APIRest::mainPage(AsyncWebServerRequest *request)
{
    ESP_LOGI(TAG, "Get main page");
    Config::load();
    strcpy(buffer_page, head_page);
    strcat(buffer_page, begin_body);
    strcat(buffer_page, menu_page);
    strcat(buffer_page, main_page);
    strcat(buffer_page, end_page);
    request->send_P(200, "text/html", buffer_page, processor);
}

void APIRest::connectionConfig(AsyncWebServerRequest *request)
{
    ESP_LOGI(TAG, "Get Connection Config");
    Config::load();
    strcpy(buffer_page, head_page);
    strcat(buffer_page, begin_body);
    strcat(buffer_page, menu_page);
    strcat(buffer_page, connection_config);
    strcat(buffer_page, end_page);
    request->send_P(200, "text/html", buffer_page, processor);
}
/*
void APIRest::saveConnection(AsyncWebServerRequest *request)
{
    AsyncWebParameter *p1 = request->getParam(0);
    Config::wifiSSID = p1->value().c_str();

    AsyncWebParameter *p2 = request->getParam(1);
    Config::wifiHostname = p2->value().c_str();

    AsyncWebParameter *p3 = request->getParam(2);
    Config::wifiPassword = p3->value().c_str();

    String staticIP = (request->getParam(3))->value().c_str();
    Config::staticIP = staticIP.c_str();

    String staticGW = (request->getParam(4))->value().c_str();
    Config::staticGW = staticGW.c_str();

    String staticSN = (request->getParam(5))->value().c_str();
    Config::staticSN = staticSN.c_str();

    Config::save();
    initWifi();
    request->redirect("/main");
}

void APIRest::messageManager(AsyncWebServerRequest *request)
{
}
*/
void APIRest::saveConfig(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total)
{
    String bodyContent = getBodyContent(data, len);
    Serial.println("Saving configuration...");
    if (Config::setConfig(bodyContent))
    {
        NetConnection *nc = NetConnection::getInstance();
        Config::save();
        request->send(200, "application/json", bodyContent);
        nc->connectWiFi_STA(true);
    }
    else
    {
        request->send(400);
        return;
    }
}

void APIRest::gpio(AsyncWebServerRequest *request, uint8_t *data, size_t len, size_t index, size_t total)
{
    String bodyContent = getBodyContent(data, len);

    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, bodyContent);

    if (error)
    {
        request->send(400);
        return;
    }

    const byte gpio = doc["gpio"];
    const byte mode = doc["mode"];
    const byte action = doc["action"];

    pinMode(gpio, mode);
    digitalWrite(gpio, action);

    Serial.println(bodyContent);
    request->send(200, "application/json", bodyContent);
}
