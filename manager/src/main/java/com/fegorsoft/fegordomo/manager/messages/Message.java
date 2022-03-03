package com.fegorsoft.fegordomo.manager.messages;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface Message {
    public void pub(String msg);
    public void pub(String msg, String topic);
    public void sub(String topic);
    public void connect() throws MqttException;
}
