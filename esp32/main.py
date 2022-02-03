from mqttctrl import MqttCtrl


def sub_cb(topic, msg):
    print((topic, msg))
    mqtt_ctrl = MqttCtrl()
    mqtt_ctrl.exec(msg)


def connect_and_subscribe():
    global app_config, client_id
    client = MQTTClient(client_id,
                        app_config["mqtt_server"], 
                        app_config["mqtt_port"], 
                        app_config["mqtt_user"], 
                        app_config["mqtt_passwd"])
    client.set_callback(sub_cb)
    client.connect()
    client.subscribe(app_config["mqtt_topic_sub"])
    print('Connected to %s MQTT broker, subscribed to %s topic' % 
          (app_config["mqtt_server"], 
           app_config["mqtt_topic_sub"]))
    return client


def restart_and_reconnect():
    print('Failed to connect to MQTT broker. Reconnecting...')
    rtc = machine.RTC()
    print(rtc.datetime())
    time.sleep(10)
    machine.reset()
    connect_and_subscribe()


try:
    client = connect_and_subscribe()
except OSError as e:
    restart_and_reconnect()

i = 0
while True:
    try:
        i += 1
        client.check_msg()
        #print(i, " ", sep=",", end="")
        # time.sleep(1)
        # if (time.time() - last_message) > message_interval:
        # msg = b'Hello #%d' % counter
        #client.publish(topic_pub, msg)
        #last_message = time.time()
        #counter += 1
    except OSError as err:
        print("OS error: {0}".format(err))
        restart_and_reconnect()
