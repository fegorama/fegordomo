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

@Entity
@Table (name = "GPIOS")
public class GPIO implements PeripheralInterface {
    enum modes { INPUT, OUTPUT }

    @Id
    @Column(name = "ID_PK", nullable=false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private byte gpio;

    @Column(name = "MODE")
    private byte mode;

    @Column(name = "STATUS")
    private boolean status;
    
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
}
