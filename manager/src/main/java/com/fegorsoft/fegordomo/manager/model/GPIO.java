package com.fegorsoft.fegordomo.manager.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table (name = "GPIOS")
public class GPIO implements PeripheralInterface {
    enum modes { INPUT, OUTPUT }

    @Id
    @Column(name = "ID_PK", nullable=false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name = "GPIO")
    private byte gpio;

    @Column(name = "MODE")
    private byte mode;

    @Column(name = "STATUS")
    private boolean status;
    
    @NotNull
    @Column(name = "DATE_TIME_ON")
    private LocalDateTime dateTimeOn;

    @NotNull
    @Column(name = "TIME_ZONE")
    private ZoneId timeZone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FK", referencedColumnName = "ID_PK")
    Device device;

    @Override
    public void off() {
        status = false;
    }

    @Override
    public void on() {
        status = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public LocalDateTime getDateTimeOn() {
        return dateTimeOn;
    }

    public void setDateTimeOn(LocalDateTime dateTimeOn) {
        this.dateTimeOn = dateTimeOn;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
