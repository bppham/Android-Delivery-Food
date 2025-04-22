package com.example.ptitdelivery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.network.ApiClient;
import com.example.ptitdelivery.network.service.AuthApi;
import com.example.ptitdelivery.model.Login.LoginRequest;
import com.example.ptitdelivery.model.Login.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AuthApi authApi;
    Button btnLogin;
    EditText edtLoginEmail, edtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        authApi = ApiClient.getClient().create(AuthApi.class);
        btnLogin = findViewById(R.id.btnLogin);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        btnLogin.setOnClickListener(view -> {
            String email = edtLoginEmail.getText().toString().trim();
            String password = edtLoginPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            login(email, password);
        });
    };

    private void login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        authApi.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    // Lưu token & id vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", "Bearer " + loginResponse.getToken());
                    editor.putString("id", loginResponse.get_id()); // Lưu id của shipper
                    editor.apply();
                    // Chuyển hướng đến MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("TOKEN", loginResponse.getToken());
                    startActivity(intent);
                    finish(); // Đóng LoginActivity để không quay lại được nữa
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}