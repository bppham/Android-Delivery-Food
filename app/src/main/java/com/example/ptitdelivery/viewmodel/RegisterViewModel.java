package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.Shipper.ShipperRegisterRequest;
import com.example.ptitdelivery.model.Shipper.ShipperRegisterResponse;
import com.example.ptitdelivery.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    private AuthRepository repository;
    public RegisterViewModel() {
        this.repository = new AuthRepository();
    }
    private final MutableLiveData<ShipperRegisterResponse> registerResponse = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void registerShipper(ShipperRegisterRequest request) {
        repository.registerShipper(request, new Callback<ShipperRegisterResponse>() {
            @Override
            public void onResponse(Call<ShipperRegisterResponse> call, Response<ShipperRegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registerResponse.setValue(response.body());
                } else {
                    errorMessage.setValue("Đăng ký thất bại");
                }
            }

            @Override
            public void onFailure(Call<ShipperRegisterResponse> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }
    public LiveData<ShipperRegisterResponse> getRegisterResponse() {
        return registerResponse;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
