package com.example.ptitdelivery.network;

import com.example.ptitdelivery.network.interceptor.AuthInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.1.10:5000/api/v1/";
    // Dùng khi chưa có token (login, register)
    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)  // ✅ thêm dòng này để sử dụng client có interceptor
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
