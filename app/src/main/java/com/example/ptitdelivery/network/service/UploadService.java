package com.example.ptitdelivery.network.service;

import com.example.ptitdelivery.model.Image;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("/api/v1/upload/avatar")
    Call<ResponseBody> uploadAvatar(@Part MultipartBody.Part file);
    @Multipart
    @POST("/api/v1/upload/images")
    Call<List<Image>> uploadImages(@Part List<MultipartBody.Part> files);

    @DELETE("/api/v1/upload/delete-file")
    Call<ResponseBody> deleteFile();
}
