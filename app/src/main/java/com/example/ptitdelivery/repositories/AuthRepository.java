package com.example.ptitdelivery.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.ptitdelivery.model.Login.LoginRequest;
import com.example.ptitdelivery.model.Login.LoginResponse;
import com.example.ptitdelivery.model.Shipper.ShipperRegisterRequest;
import com.example.ptitdelivery.model.Shipper.ShipperRegisterResponse;
import com.example.ptitdelivery.network.ApiClient;
import com.example.ptitdelivery.network.service.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final AuthService authService;

    public AuthRepository() {
        // Khởi tạo AuthService bằng Retrofit
        authService = ApiClient.getClient().create(AuthService.class);
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

}
