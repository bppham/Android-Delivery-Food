package com.example.ptitdelivery.network.service;

import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.Chat.Chat;
import com.example.ptitdelivery.model.Chat.Message;
import com.example.ptitdelivery.model.Chat.MessageResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatService {
    @POST("/api/v1/chat/{id}")
    Call<Chat> createChat(@Path("id") String id, @Body String storeId);
    @POST("/api/v1/message/{id}")
    Call<ApiResponse<Message>> sendMessage(@Path("id") String id, @Body Map<String, Object> data);
    @GET("/api/v1/chat/")
    Call<List<Chat>> getAllChats();
    @GET("/api/v1/message/{id}")
    Call<MessageResponse> getAllMessages(@Path("id") String id);
    @DELETE("/api/v1/chat/delete/{id}")
    Call<ApiResponse<String>> deleteChat(@Path("id") String id);
    @DELETE("/api/v1/message/delete/{id}")
    Call<ApiResponse<String>> deleteMessage(@Path("id") String id);
}
