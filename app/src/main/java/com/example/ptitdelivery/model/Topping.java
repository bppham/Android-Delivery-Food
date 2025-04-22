package com.example.ptitdelivery.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Topping implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private int price;
    private String toppingGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getToppingGroup() {
        return toppingGroup;
    }

    public void setToppingGroup(String toppingGroup) {
        this.toppingGroup = toppingGroup;
    }
}
