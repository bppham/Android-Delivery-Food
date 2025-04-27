package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.ChangePassword.ChangePasswordResponse;
import com.example.ptitdelivery.model.ChangePassword.ResetPasswordRequest;
import com.example.ptitdelivery.model.ChangePassword.VerifyOldPasswordRequest;
import com.example.ptitdelivery.model.Shipper.Shipper;
import com.example.ptitdelivery.repositories.ShipperRepository;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Shipper> shipper = new MutableLiveData<>();
    private final MutableLiveData<ChangePasswordResponse> responseChangePassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUpdateSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private ShipperRepository repository;
    public void init(String token) {
        repository = new ShipperRepository(token);
    }
    public void getShipper(String id) {
        repository.getShipper(id, shipper, isLoading, errorMessage);
    }
    public void updateShipper(Shipper shipperToUpdate) {
        repository.updateShipper(shipperToUpdate, shipper, isLoading, errorMessage);
    }
    public void verifyOldPassword(VerifyOldPasswordRequest request) {
        repository.verifyOldPassword(request, responseChangePassword, isLoading, errorMessage);
    }
    public void resetPassword(ResetPasswordRequest request) {
        repository.resetPassword(request, responseChangePassword, isLoading, errorMessage);
    }

    public LiveData<ChangePasswordResponse> getResponseChangePassword() {
        return responseChangePassword;
    }

    public LiveData<Shipper> getShipperLiveData() {
        return shipper;
    }
    public LiveData<Boolean> getIsUpdateSuccess() {
        return isUpdateSuccess;
    }
    public LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }
    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }
}
