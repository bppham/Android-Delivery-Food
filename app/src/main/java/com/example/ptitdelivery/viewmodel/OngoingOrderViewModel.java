package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.network.retrofit.AuthRetrofitFactory;
import com.example.ptitdelivery.repositories.OrderRepository;

public class OngoingOrderViewModel extends ViewModel {
    private OrderRepository repository;
    public OngoingOrderViewModel() {
        if (!AuthRetrofitFactory.isInitialized()) {
            throw new IllegalStateException("AuthRetrofitFactory chưa được khởi tạo! Phải login trước.");
        }
        this.repository = new OrderRepository();
    }
    private final MutableLiveData<Order> takenOrder = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Order> updatedOrder = new MutableLiveData<>();
    public void getTakenOrder() {
        repository.getTakenOrder(takenOrder, errorMessage, isLoading);
    }
    public LiveData<Order> getTakenOrderLiveData() {
        return takenOrder;
    }
    public void updateOrderStatus(String orderId, String status) {
        repository.updateOrderStatus(orderId, status, updatedOrder, errorMessage, isLoading);
    }
    public LiveData<Order> getUpdatedOrderLiveData() {
        return updatedOrder;
    }
    public LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }
    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }

}
