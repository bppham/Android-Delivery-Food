package com.example.ptitdelivery.model;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private String name;
    private String number;

    public Vehicle() {
    }

    public Vehicle(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
