import ujson

class Message:
    def __init__(self):
        print("Create message")
        
    def exec(self, msg):
        parsed = ujson.loads(msg)
        print(parsed)
