package com.meraki.capstone;


public class FB_RegisterData {
    public String newkeycode = "";
    public boolean newuser = false;

    public FB_RegisterData() {

    }

    public FB_RegisterData(String newkeycode, boolean newuser) {
        this.newkeycode = newkeycode;
        this.newuser = newuser;
    }

    public String getNewkeycode() {
        return newkeycode;
    }

    public void setNewkeycode(String newkeycode) {
        this.newkeycode = newkeycode;
    }

    public boolean isNewuser() {
        return newuser;
    }

    public void setNewuser(boolean newuser) {
        this.newuser = newuser;
    }
}

