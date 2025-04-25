package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.repositories.OrderRepository;

import java.util.List;

public class NewOrdersViewModel extends ViewModel {
    private final MutableLiveData<List<Order>> orders = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isOrderAccepted = new MutableLiveData<>();

    private OrderRepository repository;

    public void init(String token) {
        repository = new OrderRepository(token);
    }
    public LiveData<List<Order>> getOrders() {
        return orders;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public MutableLiveData<Boolean> getIsOrderAccepted() {
        return isOrderAccepted;
    }
    public void fetchOrders() {
        if (repository != null) {
            repository.getOrders(orders, isLoading, errorMessage);
        } else {
            errorMessage.setValue("Repository chưa được khởi tạo.");
        }
    }
    public void acceptOrder(String orderId) {
        repository.acceptOrder(orderId, isOrderAccepted, errorMessage);
    }

}
