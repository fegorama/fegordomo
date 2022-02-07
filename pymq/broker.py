#!/usr/bin/python

from socket import socket, gethostname

class broker:
    def __init__(self, port = 8989):
        self.port = port
        self.hostname = ""
        
    def start(self):
        self.skt = socket()
        self.hostname = gethostname()
        self.skt.bind((self.hostname, self.port))
        
        print("Starting: ", self.hostname, ", ", self.port)
        
        self.skt.listen()
        
        end_connection = False
        
        c, addr = self.skt.accept()
        print("Connected!")
        
        while not end_connection:
            r = c.recv(128).decode("utf-8")
            print("Recive: ", r)
                
            if r == "0":
                end_connection = True
        
        c.close()
        self.skt.close()
        print("Close connection")

brk = broker()
brk.start()
