package com.example.ptitdelivery.model.ChangePassword;

import java.io.Serializable;

public class ResetPasswordRequest implements Serializable {
    private String newPassword;

    public ResetPasswordRequest(String newPassword) {
        this.newPassword = newPassword;
    }

}
