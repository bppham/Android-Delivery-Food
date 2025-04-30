package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.CheckOTP.CheckOTPRequest;
import com.example.ptitdelivery.model.CheckOTP.CheckOTPResponse;
import com.example.ptitdelivery.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOTPViewModel extends ViewModel {
    private AuthRepository repository;
    public CheckOTPViewModel() {
        this.repository = new AuthRepository();
    }
    private final MutableLiveData<CheckOTPResponse> checkOTPResponse = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public void checkOTP (CheckOTPRequest request) {
        repository.checkOTP(request, new Callback<CheckOTPResponse>() {
            @Override
            public void onResponse(Call<CheckOTPResponse> call, Response<CheckOTPResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    checkOTPResponse.setValue(response.body());
                } else {
                    errorMessage.setValue("OTP không hợp lệ");
                }
            }

            @Override
            public void onFailure(Call<CheckOTPResponse> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }
    public LiveData<CheckOTPResponse> getCheckOTPResponse() {
        return checkOTPResponse;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

}
