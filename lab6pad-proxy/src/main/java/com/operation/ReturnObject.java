package com.operation;

public class ReturnObject {
    private int status;
    private String rns;

    public ReturnObject(int status, String rns) {
        this.status = status;
        this.rns = rns;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRns() {
        return rns;
    }

    public void setRns(String rns) {
        this.rns = rns;
    }
}
