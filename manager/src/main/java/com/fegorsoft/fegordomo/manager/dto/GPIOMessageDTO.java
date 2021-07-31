package com.fegorsoft.fegordomo.manager.dto;

public class GPIOMessageDTO {
    private String gpio; // gpio (1 to 15)
    private String mode; // input or output (1 or 2)
    private String action; // hight or low (1 or 0)

    public GPIOMessageDTO() {
    }

    public GPIOMessageDTO(String gpio, String mode, String action) {
        this.gpio = gpio;
        this.mode = mode;
        this.action = action;
    }

    public String getGpio() {
        return gpio;
    }

    public void setGpio(String gpio) {
        this.gpio = gpio;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
