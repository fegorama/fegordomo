package com.fegorsoft.fegordomo.manager.messages;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class MQTTService implements Message, MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(MQTTService.class);

    @Value("${mqtt.url}")
    private String url;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.client-id}")
    private String defaultClientId;

    @Value("${mqtt.default-topic}")
    private String defaultTopic;

    @Value("${mqtt.timeout}")
    private int timeout;

    @Value("${mqtt.keepalive}")
    private int keepAlive;

    private static final String clientId = MqttAsyncClient.generateClientId();

    public MqttClient client;
    public MqttTopic topic;
    public MqttMessage message;

    public MQTTService() {
        log.info("Call to MQTTService");

    }

    @Override
    public void pub(String msg) {
        pub(msg, defaultTopic);

    }

    @Override
    public void pub(String msg, String subject) {
        try {
            message = new MqttMessage();
            message.setQos(0);
            message.setRetained(false);
            message.setPayload(msg.getBytes());
            topic = client.getTopic(subject != null ? subject : defaultTopic);

            if (null == topic) {
                log.error("Topic not exist!");
            }

            MqttDeliveryToken token = topic.publish(message);
            token.waitForCompletion();
            Thread.sleep(100);
            log.info("Message is published completely: {}, topic: '{}', message: '{}']",
                    token.isComplete() ? "true" : "false", topic.getName(), msg);

        } catch (MqttException | InterruptedException e) {
            log.error("Error in MQTT: {}", e.getMessage());
        }
    }

    @Override
    public void sub(String topic) {
        log.info("Start subscribing to topics: {}", topic);

        try {
            if (client != null) {
                client.subscribe(topic, 0);
            }

        } catch (MqttException e) {
            log.error("Error in subcription: {}", e.getMessage());
        }

    }

    @Override
    public void connect() {
        try {
            if (client == null) {
                client = new MqttClient(url, defaultClientId + "_" + clientId, new MemoryPersistence());
                log.info("MQTT client: '{}' created for url: {}", defaultClientId + "_" + clientId, url);
                client.setCallback(this);
            }

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepAlive);

            if (!client.isConnected()) {
                client.connect(options);
                log.info("MQTT Connected: {}", options.toString());

            } else {
                client.disconnect();
                log.info("MQTT disconnected. Connecting...");
                client.connect(options);
            }

        } catch (MqttException e) {
            log.error("MQTT error!: {}", e.getMessage());
        }
    }

    /*
     * Callback from setCallback
     */

    @Override
    public void connectionLost(Throwable me) {
        log.error("Connection lost!");
        log.info("Message: " + me.getMessage());
        log.info("Localized: " + me.getLocalizedMessage());
        log.info("Cause: " + me.getCause());
        log.info("Except: " + me);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("Message arrived: {}, {}", s, mqttMessage.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("Message delivered");
        try {
            MqttDeliveryToken token = (MqttDeliveryToken) iMqttDeliveryToken;
            log.info("Token: {}", token.getClient().getClientId());
            if (token.getMessage() != null) {
                String h = token.getMessage().toString();
                log.info("Deliverd message :" + h);
            }

        } catch (MqttException me) {
            log.error("Reason: " + me.getReasonCode());
            log.info("Message: " + me.getMessage());
            log.info("Localized: " + me.getLocalizedMessage());
            log.info("Cause: " + me.getCause());
            log.info("Except: " + me);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
