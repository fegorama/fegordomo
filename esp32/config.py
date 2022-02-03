import ujson
import os


class Config:
    def __init__(self):
        self.app_config = {
            "ssid": "SKYNET",
            "password": "Manual_de_BASIC2",
            "mqtt_server": "192.168.2.2",
            "mqtt_port": "1883",
            "mqtt_user": "fegordomo",
            "mqtt_passwd": "fegordomo",
            "mqtt_topic_sub": "Pool",
            "mqtt_topic_pub": "Central"
        }

    def save(self):
        f = open("config.json", "w")
        s = ujson.dumps(self.app_config)
        f.write(s)
        f.close()

    def load(self):
        try:
            f = open("config.json", "r")
            s = f.read()
            f.close()
            self.app_config = ujson.loads(s)
            return self.app_config
        except OSError:
            self.save()
            return self.app_config

    def delete(self):
        os.remove("config.json")
