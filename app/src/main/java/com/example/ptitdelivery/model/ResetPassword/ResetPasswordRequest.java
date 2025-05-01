package com.example.ptitdelivery.model.ResetPassword;

import java.io.Serializable;

public class ResetPasswordRequest implements Serializable {
    private String email;
    private String password;

    public ResetPasswordRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
