package com.example.ptitdelivery.utils;

import android.app.AlertDialog;
import android.content.Context;

public class DialogHelper {
    public static void showErrorDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("❌ Lỗi")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(true)
                .show();
    }

    // ✅ Dialog thành công
    public static void showSuccessDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("✅ Thành công")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(true)
                .show();
    }

    // ❓ Dialog xác nhận - có callback
    public static void showConfirmDialog(Context context, String message, ConfirmCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle("❓ Bạn có chắc không?")
                .setMessage(message)
                .setPositiveButton("Có", (dialog, which) -> {
                    if (callback != null) {
                        callback.onConfirmed();
                    }
                })
                .setNegativeButton("Không", null)
                .setCancelable(true)
                .show();
    }

    // Giao diện callback cho confirm
    public interface ConfirmCallback {
        void onConfirmed();
    }
}
