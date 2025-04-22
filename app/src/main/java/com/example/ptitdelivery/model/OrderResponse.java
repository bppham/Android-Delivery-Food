package com.example.ptitdelivery.model;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {
    public boolean success;
    public int count;
    public List<Order> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
