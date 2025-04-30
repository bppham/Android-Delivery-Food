package com.example.ptitdelivery.model.ForgetPassword;

import java.io.Serializable;

public class ForgetPasswordResponse implements Serializable {
    private int code;
    private String status;
    private String message;

    // Getters
    public int getCode() { return code; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }

}
