package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.ChangePassword.ChangePasswordResponse;
import com.example.ptitdelivery.model.ChangePassword.ResetPasswordRequest;
import com.example.ptitdelivery.model.ChangePassword.VerifyOldPasswordRequest;
import com.example.ptitdelivery.model.Shipper.Shipper;
import com.example.ptitdelivery.network.retrofit.AuthRetrofitFactory;
import com.example.ptitdelivery.repositories.ShipperRepository;

import java.io.File;

public class ProfileViewModel extends ViewModel {
    private ShipperRepository repository;
    public ProfileViewModel() {
        if (!AuthRetrofitFactory.isInitialized()) {
            throw new IllegalStateException("AuthRetrofitFactory chưa được khởi tạo! Phải login trước.");
        }
        this.repository = new ShipperRepository();
    }
    private final MutableLiveData<Shipper> shipper = new MutableLiveData<>();
    private final MutableLiveData<ChangePasswordResponse> responseChangePassword = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUpdateSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> imageUrl = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public void getShipper(String id) {
        repository.getShipper(id, shipper, isLoading, errorMessage);
    }
    public void updateShipper(Shipper shipperToUpdate) {
        repository.updateShipper(shipperToUpdate, shipper, isLoading, errorMessage, isUpdateSuccess);
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
    public void uploadAvatar(File imageFile) {
        repository.uploadAvatar(imageFile, imageUrl, errorMessage, isLoading);
    }
    public LiveData<String> getImageUrlLiveData() {
        return imageUrl;
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
