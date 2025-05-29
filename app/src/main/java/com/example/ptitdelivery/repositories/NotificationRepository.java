package com.example.ptitdelivery.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.Notification;
import com.example.ptitdelivery.network.retrofit.AuthRetrofitFactory;
import com.example.ptitdelivery.network.service.NotificationService;
import com.example.ptitdelivery.utils.Resource;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationRepository {
    private final NotificationService notificationService;
    public NotificationRepository() {
        Retrofit retrofit = AuthRetrofitFactory.getInstance().createClient();
        this.notificationService = retrofit.create(NotificationService.class);
    }

    public LiveData<com.example.ptitdelivery.utils.Resource<ApiResponse<List<Notification>>>> getAllNotifications() {
        MutableLiveData<Resource<ApiResponse<List<Notification>>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        notificationService.getAllNotifications().enqueue(new Callback<ApiResponse<List<Notification>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NotificationRepository", "getAllNotifications: " + response.body());
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
            public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ApiResponse<List<Notification>>>> updateNotificationStatus(String id) {
        MutableLiveData<Resource<ApiResponse<List<Notification>>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        notificationService.updateNotificationStatus(id).enqueue(new Callback<ApiResponse<List<Notification>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NotificationRepository", "updateNotificationStatus: " + response.body());
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
            public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
