package com.example.ptitdelivery.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ptitdelivery.R;
import com.example.ptitdelivery.activities.LoginActivity;
import com.example.ptitdelivery.network.ApiClient;
import com.example.ptitdelivery.network.service.ShipperApi;
import com.example.ptitdelivery.model.Shipper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private ImageView ivProfileAvatar;
    private TextView tvEmail1, tvName, tvEmail2, tvGender, tvPhoneNumber;
    private Button btnLogout;
    private ShipperApi shipperApi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivProfileAvatar = view.findViewById(R.id.iv_profile_image);
        tvEmail1 = view.findViewById(R.id.tv_profile_email_1);
        tvName = view.findViewById(R.id.tv_profile_name);
        tvEmail2 = view.findViewById(R.id.tv_profile_email_2);
        tvGender = view.findViewById(R.id.tv_profile_gender);
        tvPhoneNumber = view.findViewById(R.id.tv_profile_phoneNumber);
        btnLogout = view.findViewById(R.id.btn_profile_logout);

        // Lấy ID & Token từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        String token = sharedPreferences.getString("token", null);

        if (id != null && token != null) {
            shipperApi = ApiClient.getClient().create(ShipperApi.class);
            getShipperInfo(id, token);
        }

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();  // Xóa dữ liệu khi đăng xuất
            editor.apply();
            Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    private void getShipperInfo(String id, String token) {
        shipperApi.getShipper(id, token).enqueue(new Callback<Shipper>() {
            @Override
            public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                if (response.isSuccessful()) {
                    Shipper shipper = response.body();
                    tvEmail1.setText(shipper.getEmail());
                    tvEmail2.setText(shipper.getEmail());
                    tvName.setText(shipper.getName());
                    tvGender.setText(shipper.getGender());
                    tvPhoneNumber.setText(shipper.getPhonenumber());
                    if (shipper.getAvatar() != null) {
                        Glide.with(getActivity())
                                .load(shipper.getAvatar().getUrl())  // Dùng URL từ đối tượng Avatar
                                .placeholder(R.drawable.ic_profile)
                                .into(ivProfileAvatar);
                    } else {
                        ivProfileAvatar.setImageResource(R.drawable.ic_profile);
                    }

                } else {
                    Toast.makeText(getActivity(), "Không lấy được thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Shipper> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
