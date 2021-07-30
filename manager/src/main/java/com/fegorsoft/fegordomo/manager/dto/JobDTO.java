package com.fegorsoft.fegordomo.manager.dto;

import java.util.List;

public class JobDTO {
    String name;
    String group;
    List<TriggerDTO> triggerDTO;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<TriggerDTO> getTriggerDTO() {
        return triggerDTO;
    }

    public void setTriggerDTO(List<TriggerDTO> triggerDTO) {
        this.triggerDTO = triggerDTO;
    }

}
