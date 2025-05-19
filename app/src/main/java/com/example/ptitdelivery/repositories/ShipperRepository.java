package com.example.ptitdelivery.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.ptitdelivery.model.Avatar;
import com.example.ptitdelivery.model.ChangePassword.ChangePasswordResponse;
import com.example.ptitdelivery.model.ChangePassword.ResetPasswordRequest;
import com.example.ptitdelivery.model.ChangePassword.VerifyOldPasswordRequest;
import com.example.ptitdelivery.model.Shipper.Shipper;
import com.example.ptitdelivery.network.ApiClient;
import com.example.ptitdelivery.network.service.ShipperService;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipperRepository {
    private final ShipperService shipperService;
    public ShipperRepository(String token) {
        this.shipperService = ApiClient.getClient(token).create(ShipperService.class);
    }
    public void getShipper(String id, MutableLiveData<Shipper> shipper, MutableLiveData<Boolean> isLoading, MutableLiveData<String> errorMessage) {
        isLoading.setValue(true);
        shipperService.getShipper(id).enqueue(new Callback<Shipper>() {

            @Override
            public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    shipper.setValue(response.body());
                } else {
                    errorMessage.setValue("Không lấy được thông tin shipper");
                }
            }

            @Override
            public void onFailure(Call<Shipper> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void updateShipper(
            Shipper shipperToUpdate,
            MutableLiveData<Shipper> shipper,
            MutableLiveData<Boolean> isLoading,
            MutableLiveData<String> errorMessage,
            MutableLiveData<Boolean> isUpdateSuccess) {

        isLoading.setValue(true);
        shipperService.updateShipper(shipperToUpdate).enqueue(new Callback<Shipper>() {
            @Override
            public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    shipper.setValue(response.body());
                    isUpdateSuccess.setValue(true);
                } else {
                    errorMessage.setValue("Không thể cập nhật thông tin shipper");
                    isUpdateSuccess.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Shipper> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
                isUpdateSuccess.setValue(false);
            }
        });
    }
    public void verifyOldPassword(VerifyOldPasswordRequest request,
                                  MutableLiveData<ChangePasswordResponse> responseLiveData,
                                  MutableLiveData<Boolean> isLoading,
                                  MutableLiveData<String> errorMessage) {
        isLoading.setValue(true);
        shipperService.verifyOldPassword(request).enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.setValue(response.body());
                } else {
                    errorMessage.setValue("Mật khẩu cũ không đúng");
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    public void resetPassword(ResetPasswordRequest request,
                              MutableLiveData<ChangePasswordResponse> responseLiveData,
                              MutableLiveData<Boolean> isLoading,
                              MutableLiveData<String> errorMessage) {
        isLoading.setValue(true);
        shipperService.resetPassword(request).enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    responseLiveData.setValue(response.body());
                } else {
                    errorMessage.setValue("Không thể đổi mật khẩu");
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void uploadAvatar(File imageFile,
                                  MutableLiveData<String> imageUrl,
                                  MutableLiveData<String> errorMessage,
                                  MutableLiveData<Boolean> isLoading) {
        isLoading.setValue(true);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        shipperService.uploadImage(body).enqueue(new Callback<List<Avatar>>() {
            @Override
            public void onResponse(Call<List<Avatar>> call, Response<List<Avatar>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    imageUrl.setValue(response.body().get(0).getUrl());
                } else {
                    errorMessage.setValue("Upload ảnh thất bại");
                }
            }

            @Override
            public void onFailure(Call<List<Avatar>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối khi upload: " + t.getMessage());
            }
        });
    }


}
