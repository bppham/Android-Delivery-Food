package com.example.ptitdelivery.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ptitdelivery.model.DeliveredOrderResponse;
import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.model.Order.OrderResponse;
import com.example.ptitdelivery.model.Order.SingleOrderResponse;
import com.example.ptitdelivery.network.ApiClient;
import com.example.ptitdelivery.network.service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private final OrderService orderService;

    public OrderRepository(String token) {
        this.orderService = ApiClient.getClient(token).create(OrderService.class);
    }
    public void getOrders(MutableLiveData<List<Order>> orders,
                          MutableLiveData<Boolean> isLoading,
                          MutableLiveData<String> errorMessage) {
        isLoading.setValue(true);

        orderService.getOrders().enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    orders.setValue(response.body().getData());
                } else {
                    errorMessage.setValue("Lỗi phản hồi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    public void acceptOrder(String orderId, MutableLiveData<Boolean> isAccepted, MutableLiveData<String> errorMessage) {
        orderService.acceptOrder(orderId).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    isAccepted.setValue(true);
                } else {
                    errorMessage.setValue("Lỗi phản hồi: " + response.code());
                    isAccepted.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
                isAccepted.setValue(false);
            }
        });
    }
    public void getTakenOrder(MutableLiveData<Order> takenOrder,
                              MutableLiveData<String> errorMessage,
                              MutableLiveData<Boolean> isLoading) {
        isLoading.setValue(true);
        orderService.getTakenOrder().enqueue(new Callback<SingleOrderResponse>() {
            @Override
            public void onResponse(Call<SingleOrderResponse> call, Response<SingleOrderResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    takenOrder.setValue(response.body().getData());
                } else {
                    errorMessage.setValue(response.body() != null ? response.body().getMessage() : "Không có đơn hàng nào.");
                }
            }

            @Override
            public void onFailure(Call<SingleOrderResponse> call, Throwable t) {
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void updateOrderStatus(String orderId, String status,
                                  MutableLiveData<Order> updatedOrder,
                                  MutableLiveData<String> errorMessage,
                                  MutableLiveData<Boolean> isLoading) {
        isLoading.setValue(true);

        Map<String, String> body = new HashMap<>();
        body.put("status", status);

        orderService.updateStatus(orderId, body).enqueue(new Callback<SingleOrderResponse>() {
            @Override
            public void onResponse(Call<SingleOrderResponse> call, Response<SingleOrderResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    updatedOrder.setValue(response.body().getData());
                } else {
                    errorMessage.setValue(response.body() != null ? response.body().getMessage() : "Không thể cập nhật trạng thái đơn hàng.");
                }
            }

            @Override
            public void onFailure(Call<SingleOrderResponse> call, Throwable t) {
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void getDeliveredOrders(int page, int limit,
                                   MutableLiveData<List<Order>> deliveredOrders,MutableLiveData<Integer> totalPages,
                                   MutableLiveData<Boolean> isLoading,
                                   MutableLiveData<String> errorMessage){
        isLoading.setValue(true);
        Log.d("DeliveredOrders", "Calling API with page: " + page + " and limit: " + limit);


        orderService.getDeliveredOrders(page, limit).enqueue(new Callback<DeliveredOrderResponse>() {

            @Override
            public void onResponse(Call<DeliveredOrderResponse> call, Response<DeliveredOrderResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    // Log response body to see the data
                    Log.d("DeliveredOrders", "Response body: " + response.body());

                    // Kiểm tra body có null không
                    if (response.body() != null) {
                        deliveredOrders.setValue(response.body().getData());
                        totalPages.setValue(response.body().getTotalPages());
                    } else {
                        errorMessage.setValue("Dữ liệu trống từ API");
                        Log.e("DeliveredOrders", "Response body is null");
                    }
                } else {
                    // Log lỗi và mã trạng thái HTTP
                    errorMessage.setValue("Lỗi phản hồi từ repo-delivered orders: " + response.code());
                    Log.e("DeliveredOrders", "Response failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DeliveredOrderResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
                Log.e("DeliveredOrders", "Response failed with error: " + t.getMessage(), t);
            }
        });
    }

}
