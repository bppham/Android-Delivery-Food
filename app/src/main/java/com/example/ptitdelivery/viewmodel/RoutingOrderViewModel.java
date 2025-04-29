package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.repositories.OrderRepository;

public class RoutingOrderViewModel extends ViewModel {
    private final MutableLiveData<Order> detailOrder = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private OrderRepository repository;
    public void init(String token) {
        repository = new OrderRepository(token);
    }

    public void getDetailOrder(String orderId) {
        repository.getOrderDetail(orderId, detailOrder, isLoading, errorMessage);
    }

    public LiveData<Order> getDetailOrderLiveData() {
        return detailOrder;
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }
    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }
}
