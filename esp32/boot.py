# Complete project details at https://RandomNerdTutorials.com

import time
from umqttsimple import MQTTClient
import ubinascii
import machine
import micropython
import network
import esp
import ntptime
import time
esp.osdebug(None)
import gc
gc.collect()
  
ssid = 'SKYNET'
password = 'Manual_de_BASIC2'
mqtt_server = b'192.168.2.100'
mqtt_port = b'1883'
mqtt_user = b'fegordomo'
mqtt_passwd = b'fegordomo'
#EXAMPLE IP ADDRESS
#mqtt_server = '192.168.1.144'
client_id = ubinascii.hexlify(machine.unique_id())
topic_sub = b'notification'
topic_pub = b'hello'

last_message = 0
message_interval = 5
counter = 0

station = network.WLAN(network.STA_IF)

station.active(True)
station.connect(ssid, password)

while station.isconnected() == False:
  pass

#if needed, overwrite default time server
ntptime.host = "1.europe.pool.ntp.org"
try:
  print("Local time before synchronization：%s" %str(time.localtime()))
  #make sure to have internet connection
  ntptime.settime()
  print("Local time after synchronization：%s" %str(time.localtime()))
except:
  print("Error syncing time")
  
print('Connection successful with SSID: %s' % ssid)
print(station.ifconfig())