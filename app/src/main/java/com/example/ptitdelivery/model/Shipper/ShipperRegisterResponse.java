package com.example.ptitdelivery.model.Shipper;

import java.io.Serializable;

public class ShipperRegisterResponse implements Serializable {
    private String message;
    private String shipperId;

    public String getMessage() {
        return message;
    }

    public String getShipperId() {
        return shipperId;
    }
}
