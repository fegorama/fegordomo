#from sqlalchemy import create_engine
#import config
#from flask import Flask
#app = Flask(__name__)

#@app.route('/')
#def hello_world():
#    return 'Hello, World!'

#dbconfig = config.app_config["database"]
#type = dbconfig["type"]
#host = dbconfig["host"]
#port = dbconfig["port"]
#database = dbconfig["database"]
#username = dbconfig["username"]
#passwd = dbconfig["passwd"]
#engine = create_engine(type + "://" + username + ":" + passwd + "@" + host + ":" + str(port) + "/" + database)
