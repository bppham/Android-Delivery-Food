package com.example.ptitdelivery.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.ptitdelivery.model.Shipper.Shipper;
import com.example.ptitdelivery.network.ApiClient;
import com.example.ptitdelivery.network.service.ShipperService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipperRepository {
    private final ShipperService shipperService;
    public ShipperRepository(String token) {
        this.shipperService = ApiClient.getClient(token).create(ShipperService.class);
    }
    public void getShipper(String id, MutableLiveData<Shipper> shipper, MutableLiveData<Boolean> isLoading, MutableLiveData<String> errorMessage) {
        isLoading.setValue(true);
        shipperService.getShipper(id).enqueue(new Callback<Shipper>() {

            @Override
            public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    shipper.setValue(response.body());
                } else {
                    errorMessage.setValue("Không lấy được thông tin shipper");
                }
            }

            @Override
            public void onFailure(Call<Shipper> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

}
