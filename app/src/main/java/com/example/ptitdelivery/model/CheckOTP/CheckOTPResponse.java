package com.example.ptitdelivery.model.CheckOTP;

import java.io.Serializable;

public class CheckOTPResponse implements Serializable {
    private int code;
    private String status;
    private String message;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
