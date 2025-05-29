package com.example.ptitdelivery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.network.retrofit.AuthRetrofitFactory;
import com.example.ptitdelivery.repositories.AuthRepository;
import com.example.ptitdelivery.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, btnFingerprint;
    private EditText edtLoginEmail, edtLoginPassword;
    private TextView tvRegister, tvForgotPassword;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        maybeLoginWithFingerprint();

        AuthRepository repository = new AuthRepository();
        btnLogin = findViewById(R.id.btnLogin);
        btnFingerprint = findViewById(R.id.btnLoginWithFingerprint);
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
            Log.d("DEBUG_LOGIN", "LoginResponse: token=" + loginResponse.getToken()
                    + ", email=" + loginResponse.getEmail()
                    + ", id=" + loginResponse.get_id());
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", "Bearer " + loginResponse.getToken());
            editor.putString("email", loginResponse.getEmail());
            editor.putString("id", loginResponse.get_id());
            editor.apply();
            AuthRetrofitFactory.init("Bearer " + loginResponse.getToken());
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
        btnFingerprint.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String email = prefs.getString("email", null);
            String token = prefs.getString("token", null);

            if (email != null && token != null) {
                boolean isEnabled = prefs.getBoolean("fingerprint_enabled_" + email, false);
                if (isEnabled) {
                    showFingerprintDialog(token);
                } else {
                    Toast.makeText(this, "Bạn chưa thiết lập đăng nhập bằng vân tay", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Bạn chưa đăng nhập để lưu thông tin vân tay", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void maybeLoginWithFingerprint() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedToken = sharedPreferences.getString("token", null);
        String savedEmail = sharedPreferences.getString("email", null);
        boolean isFingerprintEnabled = false;

        if (savedEmail != null) {
            isFingerprintEnabled = sharedPreferences.getBoolean("fingerprint_enabled_" + savedEmail, false);
        }

        Log.d("DEBUG_FP", "savedEmail = " + savedEmail);
        Log.d("DEBUG_FP", "savedToken = " + savedToken);
        Log.d("DEBUG_FP", "isFingerprintEnabled = " + isFingerprintEnabled);

        if (savedToken != null && savedEmail != null && isFingerprintEnabled) {
            showFingerprintDialog(savedToken);
        }
    }


    private void showFingerprintDialog(String savedToken) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Xác thực vân tay")
                .setSubtitle("Đăng nhập bằng vân tay đã lưu")
                .setNegativeButtonText("Huỷ")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("TOKEN", savedToken.replace("Bearer ", ""));
                        AuthRetrofitFactory.init(savedToken);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(LoginActivity.this, "Xác thực thất bại!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(LoginActivity.this, "Lỗi: " + errString, Toast.LENGTH_SHORT).show();
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
};