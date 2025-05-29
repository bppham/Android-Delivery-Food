package com.example.ptitdelivery.network.retrofit;

import com.example.ptitdelivery.network.interceptor.AuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRetrofitFactory implements RetrofitFactory {
    private static AuthRetrofitFactory instance;
    private final String token;

    private AuthRetrofitFactory(String token) {
        this.token = token;
    }

    public static void init(String token) {
        if (instance == null) {
            instance = new AuthRetrofitFactory(token);
        }
    }

    public static AuthRetrofitFactory getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AuthRetrofitFactory chưa được khởi tạo. Gọi init(token) trước.");
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    @Override
    public Retrofit createClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor(token))
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.186:5000/api/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

