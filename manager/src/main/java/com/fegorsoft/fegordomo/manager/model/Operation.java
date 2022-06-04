package com.fegorsoft.fegordomo.manager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "operations")
public class Operation implements PeripheralInterface {
    enum modes {
        INPUT, OUTPUT
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Column(name = "data")
    private String data;

    @NotBlank
    @Column(name = "mode")
    private int mode;

    @NotBlank
    @Column(name = "status")
    private boolean status;

    @NotBlank
    @Column(name = "cron_trigger_on")
    private String cronTriggerOn;

    @NotBlank
    @Column(name = "cron_trigger_off")
    private String cronTriggerOff;

    @JsonIgnore
    //@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    Device device;

    @Override
    public void off() {
        status = false;
    }

    @Override
    public void on() {
        status = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    @JsonIgnore
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("id: "); sb.append(id);
        sb.append(", data: "); sb.append(data);
        sb.append(", status: "); sb.append(status);
        sb.append(", deviceId: "); sb.append(device.getId());
        sb.append("]");
        return sb.toString();
    }
}
