package com.fegorsoft.fegordomo.manager.dto;

public class GPIODTO {
    private long id;
    private int gpio;
    private int mode;
    private boolean status;
    private String cronTriggerOn;
    private String cronTriggerOff;
    private long deviceId;

    public GPIODTO(long id, int gpio, int mode, boolean status, String cronTriggerOn, String cronTriggerOff) {
        this.id = id;
        this.gpio = gpio;
        this.mode = mode;
        this.status = status;
        this.cronTriggerOn = cronTriggerOn;
        this.cronTriggerOff = cronTriggerOff;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getGpio() {
        return gpio;
    }

    public void setGpio(int gpio) {
        this.gpio = gpio;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCronTriggerOn() {
        return cronTriggerOn;
    }

    public void setCronTriggerOn(String cronTriggerOn) {
        this.cronTriggerOn = cronTriggerOn;
    }

    public String getCronTriggerOff() {
        return cronTriggerOff;
    }

    public void setCronTriggerOff(String cronTriggerOff) {
        this.cronTriggerOff = cronTriggerOff;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

}
