package com.fegorsoft.fegordomo.manager.dto;

public class ScheduleOperation {
    private Long operationId;
    private String deviceName;
    private String data;
    private String cronTriggerOn;
    private String cronTriggerOff;
    private boolean active;

    public ScheduleOperation() {
    }

    public ScheduleOperation(Long operationId, String deviceName, String data, String cronTriggerOn,
            String cronTriggerOff,
            boolean active) {
        this.operationId = operationId;
        this.deviceName = deviceName;
        this.data = data;
        this.cronTriggerOn = cronTriggerOn;
        this.cronTriggerOff = cronTriggerOff;
        this.active = active;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
