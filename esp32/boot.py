import gc
import time
from umqttsimple import MQTTClient
import ubinascii
import machine
import micropython
import network
import esp
import ntptime
import time
from config import Config
import ujson

esp.osdebug(None)
gc.collect()

# Configuration
config = Config()
config.delete()
app_config = config.load()
print(app_config)

client_id = ubinascii.hexlify(machine.unique_id())

last_message = 0
message_interval = 5
counter = 0

# Connect to to WLAN
station = network.WLAN(network.STA_IF)
station.active(True)
station.connect(config.app_config["ssid"], config.app_config["password"])

# While to connect
while station.isconnected() == False:
    pass

print("Connection successful with SSID: %s " %
      config.app_config["ssid"], station.ifconfig(), sep=".")

# Time from ntp
ntptime.host = "1.europe.pool.ntp.org"
try:
    ntptime.settime()
except:
    print("Error syncing time!")
