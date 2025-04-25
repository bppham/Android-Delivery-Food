package com.example.ptitdelivery.network.service;

import com.example.ptitdelivery.model.Shipper.Shipper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShipperService {
    @GET("shipper/{id}")
    Call<Shipper> getShipper(@Path("id") String id);
}
