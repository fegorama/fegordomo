#!/usr/bin/python

import sys
import socket
import time
import getopt
# TODO Usar argparse en lugar de getopt

class pub:
    
    def __init__(self, host = "127.0.0.1", port = 8989):
        self.host = host
        self.port = port
        
    def send(self, msg):
        self.skt = socket.socket()
        
        self.skt.connect((self.host, self.port))
        self.skt.send(msg)
        time.sleep(1)
        self.skt.close()
        print("Sended: ", msg)          
        
def main(argv):
    try:
        opts, args = getopt.getopt(argv,"?h:m:",["help", "host", "message"])
        
    except getopt.GetoptError:
        print('pub.py -m <message>')
        sys.exit(2)
      
    for opt, arg in opts:
        if opt in ("-?", "--help"):
            print("pub.py -m <message>")
            sys.exit()
            
        elif opt in ("-h", "--host"):
            host = arg
            
        elif opt in ("-m", "--message"):
            msg = arg
            
        else:
            assert False, "unhandled option"
            
    p = pub(host)
    p.send(bytes(msg, "utf-8"))

if __name__ == "__main__":
   main(sys.argv[1:])