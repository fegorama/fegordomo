package com.fegorsoft.fegordomo.manager.dto;

import java.net.InetAddress;

public class DeviceDTO {
    private long id;
    private String name;
    private String type; // ESP32, ArduinoUNO,...
    private String ip; // IP or address

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
