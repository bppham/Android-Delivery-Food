package com.example.ptitdelivery.model.ForgetPassword;

import java.io.Serializable;

public class ForgetPasswordRequest implements Serializable {
    private String email;

    public ForgetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
