package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.Shipper.Shipper;
import com.example.ptitdelivery.repositories.ShipperRepository;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Shipper> shipper = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private ShipperRepository repository;
    public void init(String token) {
        repository = new ShipperRepository(token);
    }
    public void getShipper(String id) {
        repository.getShipper(id, shipper, isLoading, errorMessage);
    }
    public LiveData<Shipper> getShipperLiveData() {
        return shipper;
    }
    public LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }
    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }
}
