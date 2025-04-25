package com.example.ptitdelivery.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptitdelivery.R;

import java.util.Locale;

public class VerifyCodeActivity extends AppCompatActivity {
    private EditText[] codeBoxes;
    private TextView tvTimer;
    private Button btnResend, btnNext, btnReturn;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);

        codeBoxes = new EditText[] {
                findViewById(R.id.edt_code_1),
                findViewById(R.id.edt_code_2),
                findViewById(R.id.edt_code_3),
                findViewById(R.id.edt_code_4),
                findViewById(R.id.edt_code_5),
                findViewById(R.id.edt_code_6)
        };

        tvTimer = findViewById(R.id.tv_timer);
        btnResend = findViewById(R.id.btn_resend);
        btnNext = findViewById(R.id.btn_verify_code_next);
        btnReturn = findViewById(R.id.btn_verify_code_return);

        for (int i = 0; i < codeBoxes.length; i++) {
            final int index = i;
            codeBoxes[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < codeBoxes.length - 1) {
                        codeBoxes[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        codeBoxes[index - 1].requestFocus();
                    }
                }
            });
        }

        action();
    }

    private String getVerificationCode() {
        StringBuilder sb = new StringBuilder();
        for (EditText edt : codeBoxes) {
            sb.append(edt.getText().toString().trim());
        }
        return sb.toString();
    }

    private void action(){
        btnReturn.setOnClickListener(v -> {
            finish();
        });
        btnNext.setOnClickListener(v -> {
            String code = getVerificationCode();
            if (code.length() == 6) {
                // Gửi mã xác nhận lên server
                Toast.makeText(this, "Mã xác nhận: " + code, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ 6 chữ số", Toast.LENGTH_SHORT).show();
            }
        });

        btnResend.setOnClickListener(v -> {
            startTimer();
            // Gửi lại mã xác nhận ở đây (API)
            Toast.makeText(this, "Đã gửi lại mã", Toast.LENGTH_SHORT).show();
        });
    }

    private void startTimer() {
        btnResend.setEnabled(false);
        countDownTimer = new CountDownTimer(2 * 60 * 1000, 1000) { // 2 phút

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                tvTimer.setText(timeFormatted);
            }

            public void onFinish() {
                tvTimer.setText("00:00");
                btnResend.setEnabled(true);
            }

        }.start();
    }
}