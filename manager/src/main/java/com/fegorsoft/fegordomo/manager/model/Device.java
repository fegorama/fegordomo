package com.fegorsoft.fegordomo.manager.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min = 0, max = 64)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Size(min = 0, max = 32)
    @Column(name = "type", nullable = false)
    private String type; // ESP32, ArduinoUNO,...

    @NotBlank
    @Column(name = "description", nullable = false)
    String description;

    @NotBlank
    @Column(name = "enable", nullable = false)
    boolean enable;

    @JsonIgnore
    //@JsonManagedReference
    @RestResource(path = "deviceOperation", rel = "operation")
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Operation> operations;

    @JsonIgnore
    //@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devicegroup_id", referencedColumnName = "id")
    DeviceGroup deviceGroup;
    
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public DeviceGroup getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(DeviceGroup deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("id: ");
        sb.append(id);
        sb.append(", name: ");
        sb.append(name);
        sb.append(", type: ");
        sb.append(type);
        sb.append(", description. ");
        sb.append(description);
        sb.append(", enable: ");
        sb.append(enable);
        sb.append(", deviceGroup: ");
        sb.append(deviceGroup.getId());
        sb.append("]");

        return sb.toString();
    }
}
