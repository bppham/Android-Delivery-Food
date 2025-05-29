package com.example.ptitdelivery.network.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoAuthRetrofitFactory implements RetrofitFactory{
    private static final NoAuthRetrofitFactory INSTANCE = new NoAuthRetrofitFactory();

    private NoAuthRetrofitFactory() {} // private constructor

    public static NoAuthRetrofitFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public Retrofit createClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.186:5000/api/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
