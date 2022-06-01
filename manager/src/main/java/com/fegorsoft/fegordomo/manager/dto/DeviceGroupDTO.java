package com.fegorsoft.fegordomo.manager.dto;

public class DeviceGroupDTO {
    private long id;
    private String name;
    
    public DeviceGroupDTO(long id, String name) {
        this.id = id;
        this.name = name;
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

}
