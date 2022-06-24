package com.fegorsoft.fegordomo.manager.messages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fegorsoft.fegordomo.certs.CertsManager;

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

    @Value("${mqtt.jks-file}")
    private String jksFile;

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
        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

        if (client == null) {
            try {
                client = new MqttClient(url, defaultClientId + "_" + clientId, dataStore);

            } catch (MqttException e) {
                e.printStackTrace();
            }

            log.info("MQTT client: '{}' created for url: {}", defaultClientId + "_" + clientId, url);
            client.setCallback(this);
        }

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(timeout);
        options.setKeepAliveInterval(keepAlive);
        options.setCleanSession(true);

        SSLSocketFactory ssf;
        if (url.substring(0, 3).toLowerCase().equals("ssl")) {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);

            /* 
            options.setHttpsHostnameVerificationEnabled(false);

            System.setProperty("javax.net.ssl.trustStore", caCertsFile);
            System.setProperty("java.net.ssl.trustStorePassword", "iottest");
            System.setProperty("java.net.ssl.keyStorePassword", "iottest");
            */

            ssf = configureSSLSocketFactory();
            options.setSocketFactory(ssf);

        } else {
            log.warn("Connection is not over SSL!");
        }

        if (!client.isConnected()) {
            try {
                client.connect(options);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            log.info("MQTT Connected: {}", options.toString());

        } else {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
            log.info("MQTT disconnected. Connecting...");
            try {
                client.connect(options);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        // }
    }

    private SSLSocketFactory configureSSLSocketFactory() {
        KeyStore ks;
        SSLContext sc;
        SSLSocketFactory ssf = null;

        try {
            log.info("Loading JKS file: {}", jksFile);

            InputStream jksInputStream = new FileInputStream(jksFile);
            ks = KeyStore.getInstance("JKS");
            ks.load(jksInputStream, "iottest".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "iottest".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            sc = SSLContext.getInstance("TLSv1.3");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(kmf.getKeyManagers(), trustManagers, null);

            ssf = sc.getSocketFactory();

        } catch (KeyStoreException kse) {
            log.error("Key Store Error: {}", kse);

        } catch (NoSuchAlgorithmException nsae) {
            log.error("Algorithm Error: {}", nsae);

        } catch (CertificateException ce) {
            log.error("Certificate error: {}", ce);

        } catch (IOException ioe) {
            log.error("I/O Error: {}", ioe);

        } catch (UnrecoverableKeyException uke) {
            log.error("Unrecoverable Key: {}", uke);

        } catch (KeyManagementException kme) {
            log.error("Key Management Error: {}", kme);
        }

        return ssf;
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
