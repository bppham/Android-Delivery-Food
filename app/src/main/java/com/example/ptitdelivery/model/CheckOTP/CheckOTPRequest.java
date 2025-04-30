package com.example.ptitdelivery.model.CheckOTP;

import java.io.Serializable;

public class CheckOTPRequest implements Serializable {
    private String email;
    private String otp;

    public CheckOTPRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }
}
