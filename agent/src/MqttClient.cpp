#include <PubSubClient.h>
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
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");

  for (int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
  }

  Serial.println();

  // Switch on the LED if an 1 was received as first character
  if ((char)payload[0] == '1')
  {
    digitalWrite(BUILTIN_LED, LOW); // Turn the LED on (Note that LOW is the voltage level
    // but actually the LED is on; this is because
    // it is acive low on the ESP-01)
  }
  else
  {
    digitalWrite(BUILTIN_LED, HIGH); // Turn the LED off by making the voltage HIGH
  }
}

void MqttClient::reconnect()
{
  // Loop until we're reconnected
  while (!client.connected())
  {
    Serial.print("Attempting MQTT connection...");
    String clientId = "ESP32Client-";
    clientId += String(random(0xffff), HEX);
    Serial.println(clientId.c_str());
    // Attempt to connect
    if (client.connect(clientId.c_str(), "fegordomo", "fegordomo"))
    {
      Serial.println("Connected");
      // Once connected, publish an announcement...
      client.publish(mqtt_topic_publish, "hello world");
      // ... and resubscribe
      client.subscribe(mqtt_topic_receiver);
    }
    else
    {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
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
