package com.example.ptitdelivery.network.service;

import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.DeliveredOrderResponse;
import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.model.Order.OrderResponse;
import com.example.ptitdelivery.model.Order.SingleOrderResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {
    @GET("order/")
    Call<ApiResponse<List<Order>>> getUserOrder();
    @GET("order/direction/{id}")
    Call<SingleOrderResponse>getOrderDetail(@Path("id") String orderId);
    @PUT("order/{id}/cancel-order")
    Call<ApiResponse<String>> cancelOrder(@Path("id") String orderId);
    @GET("order/finished")
    Call<OrderResponse> getOrders();
    @PUT("order/{orderId}/accept")
    Call<SingleOrderResponse> acceptOrder(@Path("orderId") String orderId);
    @GET("order/taken")
    Call<SingleOrderResponse> getTakenOrder();
    @PUT("order/{orderId}/update-status")
    Call<SingleOrderResponse> updateStatus(@Path("orderId") String orderId, @Body Map<String, String> body);
    @GET("order/delivered")
    Call<DeliveredOrderResponse> getDeliveredOrders(
            @Query("page") int page,
            @Query("limit") int limit
    );
}
