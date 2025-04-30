package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.ForgetPassword.ForgetPasswordRequest;
import com.example.ptitdelivery.model.ForgetPassword.ForgetPasswordResponse;
import com.example.ptitdelivery.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordViewModel extends ViewModel {
    private AuthRepository repository;
    public ForgetPasswordViewModel() {
        this.repository = new AuthRepository();
    }
    private final MutableLiveData<ForgetPasswordResponse> forgetPasswordResponse = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public void forgetPassword(ForgetPasswordRequest request){
        repository.forgetPassword(request, new Callback<ForgetPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    forgetPasswordResponse.setValue(response.body());
                } else {
                    errorMessage.setValue("Gửi email thất bại");
                }
            }

            @Override
            public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }
    public LiveData<ForgetPasswordResponse> getForgetPasswordResponse() {
        return forgetPasswordResponse;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

}
