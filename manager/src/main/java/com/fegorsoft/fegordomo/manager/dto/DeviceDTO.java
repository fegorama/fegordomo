package com.fegorsoft.fegordomo.manager.dto;

import java.net.InetAddress;

public class DeviceDTO {
    private long id;
    private String name;
    private String type; // ESP32, ArduinoUNO,...
    private String ip; // IP or address
    private boolean enable; // If is enable

    public DeviceDTO() {

    }
    
    public DeviceDTO(long id, String name, String type, String ip, boolean enable) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ip = ip;
        this.enable = enable;
    }

    public DeviceDTO(long id, String name, String type, InetAddress ip, boolean enable) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ip = ip.getHostAddress();
        this.enable = enable;
    }

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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
