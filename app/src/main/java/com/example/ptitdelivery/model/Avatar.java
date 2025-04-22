package com.example.ptitdelivery.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Avatar implements Serializable {
    @SerializedName("url")
    private String url;
    private String filePath;

    public String getUrl() {
        return url;
    }
}
