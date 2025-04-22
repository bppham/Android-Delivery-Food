package com.example.ptitdelivery.model;

public class ApiResponse<T> {
    private boolean success;
    private int count;
    private T data;

    public T getData(){
        return data;
    }
}
