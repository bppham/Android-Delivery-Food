package com.example.ptitdelivery.model.Shipper;

import com.example.ptitdelivery.model.Vehicle;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShipperRegisterRequest implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("phonenumber")
    private String phonenumber;
    @SerializedName("password")
    private String password;
    @SerializedName("gender")
    private String gender;
    @SerializedName("status")
    private String status;
    @SerializedName("vehicle")
    private Vehicle vehicle;

    public ShipperRegisterRequest(String name, String email, String phonenumber, String password, String gender, String status, Vehicle vehicle) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.gender = gender;
        this.status = status;
        this.vehicle = vehicle;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
