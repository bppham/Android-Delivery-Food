package com.example.ptitdelivery.model;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {
    private String type;
    private List<Double> coordinates;
    private String address;

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
