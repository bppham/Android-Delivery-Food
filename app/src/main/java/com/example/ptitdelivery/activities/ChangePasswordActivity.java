package com.example.ptitdelivery.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.model.ChangePassword.ChangePasswordResponse;
import com.example.ptitdelivery.model.ChangePassword.ResetPasswordRequest;
import com.example.ptitdelivery.model.ChangePassword.VerifyOldPasswordRequest;
import com.example.ptitdelivery.utils.ConvertString;
import com.example.ptitdelivery.viewmodel.ProfileViewModel;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnConfirm;
    private Toolbar toolbar;
    private ProfileViewModel viewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        edtOldPassword = findViewById(R.id.edt_change_password_old_password);
        edtNewPassword = findViewById(R.id.edt_change_password_new_password);
        edtConfirmPassword = findViewById(R.id.edt_change_password_confirm_password);
        btnConfirm = findViewById(R.id.btn_change_password_confirm);
        progressBar = findViewById(R.id.progressBar_change_password);
        toolbar = findViewById(R.id.toolbar_change_password);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Log.d(TAG, "Stored Token: " + token);
        if (token == null) {
            Log.e(TAG, "Không tìm thấy token");
        }
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.init(token);

        observerViewModel();
        action();
    }

    private void observerViewModel() {
        // Quan sát kết quả từ ViewModel
        viewModel.getResponseChangePassword().observe(this, new Observer<ChangePasswordResponse>() {
            @Override
            public void onChanged(ChangePasswordResponse response) {
                if (response != null) {
                    String message = response.getMessage();
                    if ("Mật khẩu đúng!".equals(message)) {
                        // Mật khẩu cũ đúng, cho phép đổi mật khẩu mới
                        showToast("Đổi mật khẩu thành công");

                        // Sau khi verify mật khẩu cũ thành công, tiếp tục đổi mật khẩu mới
                        String newPassword = edtNewPassword.getText().toString();
                        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(newPassword);
                        viewModel.resetPassword(resetPasswordRequest);
                        finish();
                    } else {
                        // Mật khẩu cũ sai
                        showToast("Mật khẩu cũ không đúng. Vui lòng thử lại.");
                    }
                }
            }
        });

        // Quan sát lỗi kết nối hoặc lỗi trả về từ API
        viewModel.getErrorMessageLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    showToast("Lỗi: " + error);
                }
            }
        });

        // Quan sát trạng thái loading
        viewModel.getIsLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null) {
                    // Hiển thị hoặc ẩn progress bar
                    if (isLoading) {
                        // Hiển thị progress bar
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        // Ẩn progress bar
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public void action() {
        btnConfirm.setOnClickListener(view -> {
            String oldPassword = edtOldPassword.getText().toString();
            String newPassword = edtNewPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            // Kiểm tra các điều kiện đầu vào
            if (oldPassword.isEmpty()) {
                Toast.makeText(this, "Mật khẩu cũ không được để trống.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Mật khẩu mới không được để trống.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu mới và mật khẩu xác nhận không khớp.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify mật khẩu cũ
            VerifyOldPasswordRequest verifyOldPasswordRequest = new VerifyOldPasswordRequest(oldPassword);
            viewModel.verifyOldPassword(verifyOldPasswordRequest);
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}