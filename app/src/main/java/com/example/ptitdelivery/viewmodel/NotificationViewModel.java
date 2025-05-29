package com.example.ptitdelivery.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;


import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.Notification;
import com.example.ptitdelivery.network.retrofit.AuthRetrofitFactory;
import com.example.ptitdelivery.repositories.NotificationRepository;
import com.example.ptitdelivery.repositories.OrderRepository;
import com.example.ptitdelivery.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewModel extends ViewModel {
    private NotificationRepository repository;
    public NotificationViewModel() {
        if (!AuthRetrofitFactory.isInitialized()) {
            throw new IllegalStateException("AuthRetrofitFactory chưa được khởi tạo! Phải login trước.");
        }
        this.repository = new NotificationRepository();
    }
    private final MutableLiveData<Resource<ApiResponse<List<Notification>>>> allNotificationsResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Notification>>>> getAllNotificationsResponse() {
        return allNotificationsResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<Notification>>>> updateNotificationStatusResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Notification>>>> getUpdateNotificationStatusResponse() {
        return updateNotificationStatusResponse;
    }

    private MutableLiveData<Resource<List<Notification>>> notifications = new MutableLiveData<>();
    public LiveData<Resource<List<Notification>>> getNotificationsResponse() {
        return notifications;
    }
    public void getAllNotifications() {
        LiveData<Resource<ApiResponse<List<Notification>>>> result = repository.getAllNotifications();
        result.observeForever(new Observer<Resource<ApiResponse<List<Notification>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Notification>>> resource) {
                Log.d("NotificationViewModel", "getAllNotifications: " + resource);
                allNotificationsResponse.setValue(resource);
            }
        });
    }
    public void updateNotificationStatus(String id) {
        LiveData<Resource<ApiResponse<List<Notification>>>> result = repository.updateNotificationStatus(id);
        result.observeForever(new Observer<Resource<ApiResponse<List<Notification>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Notification>>> resource) {
                Log.d("NotificationViewModel", "updateNotificationStatus: " + resource);
                updateNotificationStatusResponse.postValue(resource);
            }
        });
    }
    public void updateNotifications(List<Notification> notificationsList) {
        // Tạo một đối tượng List<Notification> mới (dùng ArrayList để lưu trữ dữ liệu)
        List<Notification> response = new ArrayList<>(notificationsList);

        // Cập nhật LiveData với giá trị thành công
        notifications.postValue(new Resource<>(Resource.Status.SUCCESS, response, null));
    }
}
