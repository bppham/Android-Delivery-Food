package com.example.ptitdelivery.model;

import java.io.Serializable;

public class StoreAddress implements Serializable {
    private String full_address;
    private double lat;
    private double lon;

    public StoreAddress(String full_address, double lat, double lon) {
        this.full_address = full_address;
        this.lat = lat;
        this.lon = lon;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
