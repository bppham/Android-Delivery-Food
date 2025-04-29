package com.example.ptitdelivery.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ptitdelivery.model.User;
import com.google.gson.Gson;

public class SharedPreferencesHelper {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_INFO = "user_info";
    private static final String KEY_FOOD_TYPE_LIST = "food_type_list";
    private static final String KEY_STORE_LIST = "store_list";
    private static final String KEY_STORE_INFO = "store_info";
    private static final String KEY_LIST_DISH = "list_dish";

    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson(); // Initialize Gson

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUserData(String accessToken, String refreshToken, String userId) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }
    public void saveAccessToken(String accessToken) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public void saveCurrentUser(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_INFO, userJson);
        editor.apply();
    }

    public User getCurrentUser() {
        String userJson = sharedPreferences.getString(KEY_USER_INFO, null);
        return userJson != null ? gson.fromJson(userJson, User.class) : null;
    }

//    public void saveFoodTypes(List<FoodType> foodTypes) {
//        String json = gson.toJson(foodTypes);
//        editor.putString(KEY_FOOD_TYPE_LIST, json).apply();
//    }

//    public List<FoodType> getSavedFoodTypes() {
//        String json = sharedPreferences.getString(KEY_FOOD_TYPE_LIST, null);
//        if (json != null) {
//            Type type = new TypeToken<List<FoodType>>() {}.getType();
//            return gson.fromJson(json, type);
//        }
//        return new ArrayList<>();
//    }

//    public void saveStores(String key, List<Store> stores) {
//        Gson gson = new Gson();
//        String json = gson.toJson(stores);
//        sharedPreferences.edit().putString(key, json).apply();
//    }

//    public List<Store> getSavedStores(String key) {
//        String json = sharedPreferences.getString(key, null);
//        if (json != null) {
//            Type type = new TypeToken<List<Store>>() {}.getType();
//            return new Gson().fromJson(json, type);
//        }
//        return new ArrayList<>();
//    }
//
//    public void saveStoreInfo(Store storeInfo) {
//        String json = gson.toJson(storeInfo);
//        editor.putString(KEY_STORE_INFO, json).apply();
//    }
//
//    public Store getSavedStoreInfo() {
//        String json = sharedPreferences.getString(KEY_STORE_INFO, null);
//        if (json != null) {
//            Type type = new TypeToken<Store>() {}.getType();
//            return gson.fromJson(json, type);
//        }
//        return null;
//    }
//
//    public void saveListDish(List<Dish> listDish) {
//        String json = gson.toJson(listDish);
//        editor.putString(KEY_LIST_DISH, json).apply();
//    }
//
//    public List<Dish> getSavedListDish() {
//        String json = sharedPreferences.getString(KEY_LIST_DISH, null);
//        if (json != null) {
//            Type type = new TypeToken<List<Dish>>() {}.getType();
//            return gson.fromJson(json, type);
//        }
//        return null;
//    }

    public void clearUserData() {
        editor.clear();
        editor.apply();
    }

    public void clearAll() {
        editor.clear().apply();
    }
}
