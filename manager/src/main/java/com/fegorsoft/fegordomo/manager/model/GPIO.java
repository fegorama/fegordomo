package com.fegorsoft.fegordomo.manager.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "gpios")
public class GPIO implements PeripheralInterface {
    enum modes {
        INPUT, OUTPUT
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Column(name = "gpio")
    private byte gpio;

    @NotBlank
    @Column(name = "mode")
    private byte mode;

    @NotBlank
    @Column(name = "status")
    private boolean status;

    @NotBlank
    @Column(name = "cron_trigger_on")
    private String cronTriggerOn;

    @NotBlank
    @Column(name = "cron_trigger_off")
    private String cronTriggerOff;

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

    public byte getGpio() {
        return gpio;
    }

    public void setGpio(byte gpio) {
        this.gpio = gpio;
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
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

}
