package com.fegorsoft.fegordomo.manager.dto;

public class OperationMessageDTO {
    private long deviceId;
    private String deviceName;
    private long operationId;
    private String data;
    private long timestamp;
    
    public OperationMessageDTO(long deviceId, String deviceName, long operationId, String data, long timestamp) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.operationId = operationId;
        this.data = data;
        this.timestamp = timestamp;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public long getOperationId() {
        return operationId;
    }

    public void setOperationId(long operationId) {
        this.operationId = operationId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
