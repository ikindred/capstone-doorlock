package com.meraki.capstone;


public class FB_Auth {
    public String unitcode = "";
    public boolean used = false;

    public FB_Auth() {

    }

    public FB_Auth(String unitcode, boolean used) {
        this.unitcode = unitcode;
        this.used = used;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}

