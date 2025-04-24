package com.example.ptitdelivery.network.service;

import com.example.ptitdelivery.model.Login.LoginRequest;
import com.example.ptitdelivery.model.Login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login/shipper")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
