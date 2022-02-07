import ujson
import machine


class MqttCtrl:

    def exec(self, msg):
        self.json = ujson.loads(msg)
        print(self.json)

        mode = machine.Pin.IN if self.json["mode"] == "input" else machine.Pin.OUT
        pin = machine.Pin(int(self.json["gpio"]), mode)
        pin.on() if self.json["value"] == "on" else pin.off()
