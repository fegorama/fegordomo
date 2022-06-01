package com.fegorsoft.fegordomo.manager.dto;

public class DeviceDTO {
    private long id;
    private String name;
    private String type;
    private String description;
    private boolean enable;
    private long deviceGroupId;

    public DeviceDTO() {

    }
    
    public DeviceDTO(long id, String name, String type, String description, boolean enable) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

}
