import json


def get_all_devices():
    devices = read_from_file()
    devices_in_store = []
    for k, v in devices.items():
        current_device = {"id": k, **v}
        devices_in_store.append(current_device)

    return devices


def remove_device(id):
    devices = read_from_file()
    del devices[id]
    write_to_file(devices)


def update_device(id, device):
    devices = read_from_file()
    ids = devices.keys()
    devices[id] = {"name": device.name, "type": device.type, "ip": device.ip, "enable": device.enable}
    write_to_file(devices)


def add_device(device):
    devices = read_from_file()
    ids = devices.keys()
    new_id = int(ids[-1]) + 1
    devices[new_id] = {"name": device.name, "type": device.type, "ip": device.ip, "enable": device.enable}
    write_to_file(devices)


def get_device(id):
    devices = read_from_file()
    device = devices[id]
    device["id"] = id
    return device


def write_to_file(content):
    with open("./devices.json", "w") as devices:
        devices.write(json.dumps(content))


def read_from_file():
    with open("./devices.json", "r") as devices:
        return json.loads(devices.read())