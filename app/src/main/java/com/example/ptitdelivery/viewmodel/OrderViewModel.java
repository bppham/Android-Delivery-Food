package com.example.ptitdelivery.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.network.retrofit.AuthRetrofitFactory;
import com.example.ptitdelivery.repositories.OrderRepository;
import com.example.ptitdelivery.utils.Resource;

import java.util.List;

public class OrderViewModel extends ViewModel {
    private OrderRepository repository;
    public OrderViewModel() {
        if (!AuthRetrofitFactory.isInitialized()) {
            throw new IllegalStateException("AuthRetrofitFactory chưa được khởi tạo! Phải login trước.");
        }
        this.repository = new OrderRepository();
    }
    private final MutableLiveData<Resource<ApiResponse<List<Order>>>> currentOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Order>>>> getCurrentOrderResponse() {
        return currentOrderResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<Order>>>> historyOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Order>>>> getHistoryOrderResponse() {
        return historyOrderResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<Order>>> orderDetailResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<Order>>> getOrderDetailResponse() {
        return orderDetailResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> cancelOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getCancelOrderResponse() {
        return cancelOrderResponse;
    }
}
