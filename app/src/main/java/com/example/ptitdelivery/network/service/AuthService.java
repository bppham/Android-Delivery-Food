package com.example.ptitdelivery.network.service;

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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthService {
    @POST("auth/login/shipper")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("auth/register/shipper")
    Call<ShipperRegisterResponse> register(@Body ShipperRegisterRequest loginRequest);
    @POST("auth/forgot-password/shipper")
    Call<ForgetPasswordResponse> forgetPassword(@Body ForgetPasswordRequest body);
    @POST("auth/check-otp/shipper")
    Call<CheckOTPResponse> checkOTP(@Body CheckOTPRequest body);
    @PUT("auth/reset-password/shipper")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest body);
}
