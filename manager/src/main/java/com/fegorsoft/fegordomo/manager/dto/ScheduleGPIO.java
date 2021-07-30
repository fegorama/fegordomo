package com.fegorsoft.fegordomo.manager.dto;


public class ScheduleGPIO {
    private Long gpioId;
    private byte gpio;
    private String ip;
    private String cronTriggerOn;
    private String cronTriggerOff;

    public ScheduleGPIO() {
    }

    public ScheduleGPIO(Long gpioId, byte gpio, String ip, String cronTriggerOn, String cronTriggerOff) {
        this.gpioId = gpioId;
        this.gpio = gpio;
        this.ip = ip;
        this.cronTriggerOn = cronTriggerOn;
        this.cronTriggerOff = cronTriggerOff;
    }

    public Long getGpioId() {
        return gpioId;
    }

    public void setGpioId(Long gpioId) {
        this.gpioId = gpioId;
    }

    public byte getGpio() {
        return gpio;
    }

    public void setGpio(byte gpio) {
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

}
