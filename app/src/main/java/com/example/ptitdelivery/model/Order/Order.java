package com.example.ptitdelivery.model.Order;

import com.example.ptitdelivery.model.Item;
import com.example.ptitdelivery.model.Location;
import com.example.ptitdelivery.model.ShipLocation;
import com.example.ptitdelivery.model.Store;
import com.example.ptitdelivery.model.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    @SerializedName("_id")
    private String id;
    private User user;
    private Store store;
    private List<Item> items;
    private String status;
    private String paymentMethod;
    private String createdAt;
    private String updatedAt;
    private String shipper;
    private ShipLocation shipLocation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public ShipLocation getShipLocation() {
        return shipLocation;
    }

    public void setShipLocation(ShipLocation shipLocation) {
        this.shipLocation = shipLocation;
    }
}
