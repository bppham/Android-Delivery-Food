package com.example.ptitdelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptitdelivery.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    private Button btnForgetPasswordNext, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        btnForgetPasswordNext = findViewById(R.id.btn_forget_password_next);
        btnReturn = findViewById(R.id.btn_forget_password_return);

        action();

    }

    private void action(){
        btnForgetPasswordNext.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
            startActivity(intent);
        });

        btnReturn.setOnClickListener(v -> {
            finish();
        });
    }
}