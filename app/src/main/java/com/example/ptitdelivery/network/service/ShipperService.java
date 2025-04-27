package com.example.ptitdelivery.network.service;

import com.example.ptitdelivery.model.ChangePassword.ChangePasswordResponse;
import com.example.ptitdelivery.model.ChangePassword.ResetPasswordRequest;
import com.example.ptitdelivery.model.ChangePassword.VerifyOldPasswordRequest;
import com.example.ptitdelivery.model.Shipper.Shipper;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ShipperService {
    @GET("shipper/{id}")
    Call<Shipper> getShipper(@Path("id") String id);
    @PUT("shipper")
    Call<Shipper> updateShipper(@Body Shipper shipper);
    @POST("shipper/verify-password")
    Call<ChangePasswordResponse> verifyOldPassword(@Body VerifyOldPasswordRequest request);
    @PUT("shipper/reset-password")
    Call<ChangePasswordResponse> resetPassword(@Body ResetPasswordRequest request);


}
