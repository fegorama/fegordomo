import os
import uuid

class tail:
    def __init__(self, path = "./topics"):
        self.__path = path
        
    def create(self, name):
        self.__uuid = uuid.uuid1()
        self.__name = name
        self.__first = None
        self.__last = None
        
    def delete(self, uuid):
        files = os.listdir(self.__path)
        