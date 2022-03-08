import connexion
import six

from .. models.device import Device  # noqa: E501
from .. models.devices import Devices  # noqa: E501
from .. import util

from api.core import devices


def add_device(body):  # noqa: E501
    """Add a new device to the store

     # noqa: E501

    :param body: Device to add to the store
    :type body: dict | bytes

    :rtype: None
    """
    if connexion.request.is_json:
        body = Device.from_dict(connexion.request.get_json())  # noqa: E501
    
    devices.add_device(body)
    return {}, 201


def get_all_devices():  # noqa: E501
    """Gets all devices in the store

     # noqa: E501


    :rtype: Devices
    """
    devices = devices.get_all_devices()
    devices_in_store = []
    for device in devices:
        current_device = Device(id=device["id"], breed=device["breed"], name=device["name"], price=device["price"])
        devices_in_store.append(current_device)
    
    return devices_in_store, 200


def get_device(device_id):  # noqa: E501
    """Get a device in the store

     # noqa: E501

    :param device_id: The id of the device to retrieve
    :type device_id: str

    :rtype: Device
    """
    try:
        device = devices.get_device(device_id)
        response = Device(id=device.id, breed=device.breed, name=device.name, price=device.price), 200
    except KeyError:
        response = {}, 404

    return response


def remove_device(device_id):  # noqa: E501
    """Remove a device in the store

     # noqa: E501

    :param device_id: The id of the device to remove from the store
    :type device_id: str

    :rtype: None
    """
    try:
        devices.remove_device(device_id)
        response = {}, 200
    except KeyError:
        response = {}, 404

    return response


def update_device(device_id, Device):  # noqa: E501
    """Update and replace a device in the store

     # noqa: E501

    :param device_id: The id of the device to update from the store
    :type device_id: str
    :param Device: 
    :type Device: dict | bytes

    :rtype: None
    """
    if connexion.request.is_json:
        Device = Device.from_dict(connexion.request.get_json())  # noqa: E501

    try:
        devices.update_device(device_id, Device)
        response = {}, 200
    except KeyError:
        response = {}, 404

    return response