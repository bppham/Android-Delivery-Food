package com.example.ptitdelivery.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.repositories.OrderRepository;
import com.example.ptitdelivery.utils.Resource;

import java.util.List;

public class OrderViewModel extends ViewModel {
    private OrderRepository repository;

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

    public void init(String token) {
        repository = new OrderRepository(token);
    }

    public void getCurrentOrder() {
        LiveData<Resource<ApiResponse<List<Order>>>> result = repository.getUserOrder();
        result.observeForever(new Observer<Resource<ApiResponse<List<Order>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Order>>> resource) {
                currentOrderResponse.setValue(resource);
            }
        });
    }

    public void getHistoryOrder() {
        LiveData<Resource<ApiResponse<List<Order>>>> result = repository.getUserOrder();
        result.observeForever(new Observer<Resource<ApiResponse<List<Order>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Order>>> resource) {
                historyOrderResponse.setValue(resource);
            }
        });
    }

//    public void getOrderDetail(String orderId) {
//        LiveData<Resource<ApiResponse<Order>>> result = repository.getOrderDetail(orderId);
//        result.observeForever(new Observer<Resource<ApiResponse<Order>>>() {
//            @Override
//            public void onChanged(Resource<ApiResponse<Order>> resource) {
//                switch (resource.getStatus()) {
//                    case SUCCESS:
//                        if (resource.getData() != null && resource.getData().getData() != null) {
//                            Log.d("OrderDetailViewModel", "Lấy đơn hàng thành công: " + resource.getData().getData().toString());
//                        } else {
//                            Log.d("OrderDetailViewModel", "Thành công nhưng không có dữ liệu đơn hàng.");
//                        }
//                        break;
//                    case ERROR:
//                        Log.e("OrderDetailViewModel", "Lỗi khi lấy đơn hàng: " + resource.getMessage());
//                        break;
//                    case LOADING:
//                        Log.d("OrderDetailViewModel", "Đang tải dữ liệu đơn hàng...");
//                        break;
//                }
//
//                orderDetailResponse.setValue(resource);
//            }
//        });
//
//    }

    public void cancelOrder(String orderId) {
        LiveData<Resource<ApiResponse<String>>> result = repository.cancelOrder(orderId);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                cancelOrderResponse.setValue(resource);
            }
        });
    }
}
