package com.fegorsoft.fegordomo.manager.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;

import java.util.List;

import javax.persistence.CascadeType;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity
@Table(name = "device_group")
public class DeviceGroup {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min = 0, max = 64)
    @Column(name = "name", nullable = false)
    private String name;

//    @JsonIgnore
    @JsonManagedReference
    @RestResource(path = "group", rel = "device")
    @OneToMany(mappedBy = "deviceGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Device> devices;

    public DeviceGroup() {

    }
    
    public DeviceGroup(long id, @NotBlank @Size(min = 0, max = 64) String name, List<Device> device) {
        this.id = id;
        this.name = name;
        this.devices = device;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Device> getDevice() {
        return devices;
    }

    public void setDevice(List<Device> device) {
        this.devices = device;
    }
    
}
