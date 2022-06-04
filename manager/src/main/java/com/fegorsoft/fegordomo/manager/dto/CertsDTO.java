package com.fegorsoft.fegordomo.manager.dto;

public class CertsDTO {
    private String CA;
    private String client_crt;
    private String client_key;

    public CertsDTO() {
    }
    
    public CertsDTO(String cA, String client_crt, String client_key) {
        CA = cA;
        this.client_crt = client_crt;
        this.client_key = client_key;
    }

    public String getCA() {
        return CA;
    }
    public void setCA(String cA) {
        CA = cA;
    }
    public String getClient_crt() {
        return client_crt;
    }
    public void setClient_crt(String client_crt) {
        this.client_crt = client_crt;
    }
    public String getClient_key() {
        return client_key;
    }
    public void setClient_key(String client_key) {
        this.client_key = client_key;
    }

}
