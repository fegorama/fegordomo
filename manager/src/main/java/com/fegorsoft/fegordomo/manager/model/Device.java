package com.fegorsoft.fegordomo.manager.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.List;

@Entity
@Table (name = "DEVICES")
public class Device {
    @Id
    @Column(name = "ID_PK", nullable=false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name = "NAME", nullable=false)
    private String name;

    @Column(name = "TYPE", nullable=false)
    private String type;                        // ESP32, ArduinoUNO,...

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<GPIO> gpios;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public List<GPIO> getGpios() {
        return gpios;
    }

    public void setGpios(List<GPIO> gpios) {
        this.gpios = gpios;
    }

    
}
