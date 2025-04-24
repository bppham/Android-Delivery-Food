package com.example.ptitdelivery.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptitdelivery.R;

public class RegisterActivity extends AppCompatActivity {
    private Button btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        btnReturn = findViewById(R.id.btn_register_return);

        action();
    }

    private void action(){
        btnReturn.setOnClickListener(v -> {
            finish();
        });
    }
}