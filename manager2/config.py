import yaml
import os

CURR_DIR = os.getcwd()

def load_config(filename):
    with open(filename, "r") as f:
        return yaml.safe_load(f)

filename = CURR_DIR + "/config.yaml"
app_config = load_config(filename)
print (app_config)

