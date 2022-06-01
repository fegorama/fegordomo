package com.fegorsoft.fegordomo.manager.dto;

public class ScheduleOperationDTO {
    private Long deviceGroupId;
    private String deviceGroupName;
    private Long operationId;
    private Long deviceId;
    private String deviceName;
    private String data;
    private String cronTriggerOn;
    private String cronTriggerOff;
    private boolean active;

    public ScheduleOperationDTO() {
    }

    public ScheduleOperationDTO(Long deviceGroupId, String deviceGroupName, Long operationId, Long deviceId,
            String deviceName, String data, String cronTriggerOn, String cronTriggerOff, boolean active) {
        this.deviceGroupId = deviceGroupId;
        this.deviceGroupName = deviceGroupName;
        this.operationId = operationId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.data = data;
        this.cronTriggerOn = cronTriggerOn;
        this.cronTriggerOff = cronTriggerOff;
        this.active = active;
    }

    public Long getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(Long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public String getDeviceGroupName() {
        return deviceGroupName;
    }

    public void setDeviceGroupName(String deviceGroupName) {
        this.deviceGroupName = deviceGroupName;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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

}
