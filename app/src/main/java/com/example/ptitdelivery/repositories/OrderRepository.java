package com.example.ptitdelivery.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.DeliveredOrderResponse;
import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.model.Order.OrderResponse;
import com.example.ptitdelivery.model.Order.SingleOrderResponse;
import com.example.ptitdelivery.network.ApiClient;
import com.example.ptitdelivery.network.service.OrderService;
import com.example.ptitdelivery.utils.Resource;

import org.json.JSONObject;

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

    // For map
    public LiveData<Resource<ApiResponse<List<Order>>>> getUserOrder() {
        MutableLiveData<Resource<ApiResponse<List<Order>>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        orderService.getUserOrder().enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("OrderRepository", "getCurrentUser: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                        Log.d("OrderRepository", "getCurrentUser Error: " + errorMessage);
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                Log.d("OrderRepository", "getCurrentUser Error: " + t.getMessage());
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

//    public LiveData<Resource<ApiResponse<Order>>> getOrderDetail(String orderId) {
//        MutableLiveData<Resource<ApiResponse<Order>>> result = new MutableLiveData<>();
//        result.setValue(Resource.loading(null));
//
//        orderService.getOrderDetail(orderId).enqueue(new Callback<ApiResponse<Order>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.d("OrderRepository", "getOrderDetail: " + response.body());
//                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
//                } else {
//                    try {
//                        String errorMessage = response.errorBody().string();
//                        JSONObject jsonObject = new JSONObject(errorMessage);
//                        String message = jsonObject.getString("message");
//                        result.setValue(Resource.error(message, null));
//                    } catch (Exception e) {
//                        result.setValue(Resource.error("Lỗi không xác định!", null));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
//                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
//            }
//        });
//
//        return result;
//    }

    public void getOrderDetail(String orderId,
                               MutableLiveData<Order> orderDetail,
                               MutableLiveData<Boolean> isLoading,
                               MutableLiveData<String> errorMessage) {
        isLoading.setValue(true);

        orderService.getOrderDetail(orderId).enqueue(new Callback<SingleOrderResponse>() {
            @Override
            public void onResponse(Call<SingleOrderResponse> call, Response<SingleOrderResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("DetailOrder", "Response body: " + response.body());
                    orderDetail.setValue(response.body().getData());
                } else {
                    errorMessage.setValue("Lỗi phản hồi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SingleOrderResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
                Log.e("Detail", "Response failed with error: " + t.getMessage(), t);
            }

        });
    }


    public LiveData<Resource<ApiResponse<String>>> cancelOrder(String orderId) {
        MutableLiveData<Resource<ApiResponse<String>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        orderService.cancelOrder(orderId).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("OrderRepository", "getOrderDetail: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

}
