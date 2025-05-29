package com.example.ptitdelivery.repositories;

import com.example.ptitdelivery.model.CheckOTP.CheckOTPRequest;
import com.example.ptitdelivery.model.CheckOTP.CheckOTPResponse;
import com.example.ptitdelivery.model.ForgetPassword.ForgetPasswordRequest;
import com.example.ptitdelivery.model.ForgetPassword.ForgetPasswordResponse;
import com.example.ptitdelivery.model.Login.LoginRequest;
import com.example.ptitdelivery.model.Login.LoginResponse;
import com.example.ptitdelivery.model.ResetPassword.ResetPasswordRequest;
import com.example.ptitdelivery.model.ResetPassword.ResetPasswordResponse;
import com.example.ptitdelivery.model.Shipper.ShipperRegisterRequest;
import com.example.ptitdelivery.model.Shipper.ShipperRegisterResponse;
import com.example.ptitdelivery.network.retrofit.NoAuthRetrofitFactory;
import com.example.ptitdelivery.network.retrofit.RetrofitFactory;
import com.example.ptitdelivery.network.service.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthRepository {
    private final AuthService authService;

    public AuthRepository() {
        RetrofitFactory factory = NoAuthRetrofitFactory.getInstance();
        Retrofit retrofit = factory.createClient();
        this.authService = retrofit.create(AuthService.class);
    }

    public void login(String email, String password, LoginCallback callback){
        LoginRequest request = new LoginRequest(email, password);
        authService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Login failed!");
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public interface LoginCallback {
        void onSuccess(LoginResponse loginResponse);
        void onFailure(String errorMessage);
    }

    public void registerShipper(ShipperRegisterRequest request, Callback<ShipperRegisterResponse> callback) {
        authService.register(request).enqueue(callback);
    }

    public void forgetPassword(ForgetPasswordRequest request, Callback<ForgetPasswordResponse> callback) {
        authService.forgetPassword(request).enqueue(callback);
    }

    public void checkOTP(CheckOTPRequest request, Callback<CheckOTPResponse> callback) {
        authService.checkOTP(request).enqueue(callback);
    }

    public void resetPassword(ResetPasswordRequest request, Callback<ResetPasswordResponse> callback) {
        authService.resetPassword(request).enqueue(callback);
    }

}
