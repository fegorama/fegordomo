package com.fegorsoft.fegordomo.manager.dto;

public class ScheduleGPIO {
    private Long gpioId;
    private int gpio;
    private String ip;
    private String cronTriggerOn;
    private String cronTriggerOff;
    private boolean active;
    private String deviceName;

    public ScheduleGPIO() {
    }

    public ScheduleGPIO(Long gpioId, int gpio, String ip, String cronTriggerOn, String cronTriggerOff,
            boolean active) {
        this.gpioId = gpioId;
        this.gpio = gpio;
        this.ip = ip;
        this.cronTriggerOn = cronTriggerOn;
        this.cronTriggerOff = cronTriggerOff;
        this.active = active;
    }

    public Long getGpioId() {
        return gpioId;
    }

    public void setGpioId(Long gpioId) {
        this.gpioId = gpioId;
    }

    public int getGpio() {
        return gpio;
    }

    public void setGpio(int gpio) {
        this.gpio = gpio;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

}
