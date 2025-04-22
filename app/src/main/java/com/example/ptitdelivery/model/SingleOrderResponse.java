package com.example.ptitdelivery.model;

import java.io.Serializable;
import java.util.List;

public class SingleOrderResponse implements Serializable {
    public boolean success;
    public String message;
    public Order data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
