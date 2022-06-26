#include <PubSubClient.h>
#include <MessageManager.h>
#include "MqttClient.h"

// Update these with values suitable for your network.

// const char* ssid = "........";
// const char* password = "........";
// const char* mqtt_server = "broker.mqtt-dashboard.com";

// WiFiClient espClient;
// PubSubClient client(espClient);
// long lastMsg = 0;
// char msg[50];
// int value = 0;

MqttClient::MqttClient()
{
  this->lastMsg = 0;
  this->value = 0;
  this->mqtt_ip.fromString(mqtt_server);
}

void MqttClient::init()
{
  this->client.setClient(wifiClient);
  this->client.setServer(this->mqtt_ip, this->mqtt_port);
  this->client.setCallback(MqttClient::callback);
  //this->wifiClient.setCACert(ca_cert);
  //this->wifiClient.setCertificate(client_crt);
  //this->wifiClient.setPrivateKey(client_key);
}

boolean MqttClient::isConnected()
{
  return this->client.connected() ? true : false;
}

void MqttClient::loop()
{
  this->client.loop();
}

/*
void setup() {
  pinMode(BUILTIN_LED, OUTPUT);     // Initialize the BUILTIN_LED pin as an output
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}
*/

/*
void setup_wifi() {

  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}
*/

void MqttClient::callback(char *topic, byte *payload, unsigned int length)
{
  MessageManager* messageManager = new MessageManager();
  messageManager->proccess(topic, payload, length);
}

void MqttClient::reconnect()
{
  // Loop until we're reconnected
  while (!client.connected())
  {
    Serial.print("Attempting MQTT connection...");
    String clientId = "ESP32Client-" + String(random(0xffff), HEX);
    Serial.println(clientId.c_str());

    // Attempt to connect
    if (this->client.connect(clientId.c_str(), "fegordomo", "fegordomo"))
    {
      Serial.println("Connected");
      // Once connected, publish an announcement...
      this->client.publish(mqtt_topic_publish, clientId.c_str());
      // ... and resubscribe
      this->client.subscribe(mqtt_topic_receiver);
    }
    else
    {
      Serial.print("failed, rc=");
      Serial.print(this->client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void MqttClient::pub(const char *msg)
{
  this->client.publish(mqtt_topic_publish, msg);
}

/*
void loop() {

  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  long now = millis();
  if (now - lastMsg &gt; 2000) {
    lastMsg = now;
    ++value;
    snprintf (msg, 75, "hello world #%ld", value);
    Serial.print("Publish message: ");
    Serial.println(msg);
    client.publish("outTopic", msg);
  }
}
*/
