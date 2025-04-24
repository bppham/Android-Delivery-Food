package com.example.ptitdelivery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.network.ApiClient;
import com.example.ptitdelivery.network.service.AuthService;
import com.example.ptitdelivery.repositories.LoginRepository;
import com.example.ptitdelivery.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private AuthService authApi;
    private Button btnLogin;
    private EditText edtLoginEmail, edtLoginPassword;
    private TextView tvRegister, tvForgotPassword;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        authApi = ApiClient.getClient().create(AuthService.class);
        LoginRepository repository = new LoginRepository(authApi);
        btnLogin = findViewById(R.id.btnLogin);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        tvRegister = findViewById(R.id.tv_register);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new LoginViewModel(repository);
            }
        }).get(LoginViewModel.class);

        observeLogin();
        action();
    }

    private void observeLogin() {
        loginViewModel.getLoginResponse().observe(this, loginResponse -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", "Bearer " + loginResponse.getToken());
            editor.putString("id", loginResponse.get_id());
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("TOKEN", loginResponse.getToken());
            startActivity(intent);
            finish();
        });

        loginViewModel.getLoginError().observe(this, error -> {
            Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    public void action() {
        btnLogin.setOnClickListener(view -> {
            String email = edtLoginEmail.getText().toString();
            String password = edtLoginPassword.getText().toString();
            loginViewModel.login(email, password);
        });

        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

};
