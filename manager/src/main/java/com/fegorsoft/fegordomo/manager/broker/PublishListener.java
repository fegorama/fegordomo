package com.fegorsoft.fegordomo.manager.broker;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishListener extends AbstractInterceptHandler {
    private static final Logger log = LoggerFactory.getLogger(PublishListener.class);

    @Override
    public String getID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onPublish(InterceptPublishMessage message) {
        log.info("Moquette MQTT broker message intercepted: topic: {}, content: {}", message.getTopicName(),
                new String(message.getPayload().array()));
    }
}
