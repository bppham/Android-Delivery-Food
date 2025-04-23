package com.example.ptitdelivery.model;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private String name;
    private String number;

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
