package com.meraki.capstone;


public class FB_Users {
    public String keycode = "",  name = "", id;
    public boolean master = false;

    public FB_Users() {

    }

    public FB_Users(String keycode, String name, String id, boolean master) {
        this.keycode = keycode;
        this.name = name;
        this.id = id;
        this.master = master;
    }

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
}

