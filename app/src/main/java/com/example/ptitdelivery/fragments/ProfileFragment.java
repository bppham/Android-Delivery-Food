package com.example.ptitdelivery.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.ptitdelivery.R;
import com.example.ptitdelivery.activities.LoginActivity;
import com.example.ptitdelivery.network.service.ShipperService;
import com.example.ptitdelivery.utils.ConvertString;
import com.example.ptitdelivery.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {
    private static final String TAG = "Profile Fragment";
    private ProfileViewModel viewModel;
    private ProgressBar progressBar;
    private ImageView ivProfileAvatar;
    private TextView tvEmail, tvName, tvGender, tvPhoneNumber, tvVehicleName, tvVehicleNumber;
    private Button btnLogout;
    private ShipperService shipperApi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        progressBar = view.findViewById(R.id.progressBar_profile);
        ivProfileAvatar = view.findViewById(R.id.iv_profile_image);
        tvEmail = view.findViewById(R.id.tv_profile_email);
        tvName = view.findViewById(R.id.tv_profile_name);
        tvVehicleName = view.findViewById(R.id.tv_profile_vehicle_name);
        tvVehicleNumber = view.findViewById(R.id.tv_profile_vehicle_number);
        tvGender = view.findViewById(R.id.tv_profile_gender);
        tvPhoneNumber = view.findViewById(R.id.tv_profile_phoneNumber);
        btnLogout = view.findViewById(R.id.btn_profile_logout);

        // Lấy ID & Token từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        String token = sharedPreferences.getString("token", null);

        Log.d(TAG, "Stored Token: " + token);
        if (token == null) {
            Log.e(TAG, "Không tìm thấy token");
            return view;
        }

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.init(token);
        viewModel.getShipper(id);

        observeViewModel();
        logout(sharedPreferences);
        return view;
    }

    private void observeViewModel() {
        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        viewModel.getShipperLiveData().observe(getViewLifecycleOwner(), shipper -> {
            if (shipper != null) {
                Log.d(TAG, "Đã lấy được đơn hàng: " + shipper.getId());
                tvEmail.setText(shipper.getEmail());
                tvVehicleNumber.setText(shipper.getVehicle().getNumber());
                tvVehicleName.setText(shipper.getVehicle().getName());
                tvName.setText(shipper.getName());
                String displayGender = ConvertString.convertGender(shipper.getGender());
                tvGender.setText(displayGender);
                tvPhoneNumber.setText(shipper.getPhonenumber());
                if (shipper.getAvatar() != null) {
                    Glide.with(getActivity())
                            .load(shipper.getAvatar().getUrl())  // Dùng URL từ đối tượng Avatar
                            .placeholder(R.drawable.ic_profile)
                            .into(ivProfileAvatar);
                } else {
                    ivProfileAvatar.setImageResource(R.drawable.ic_profile);
                }
            }
        });
        viewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Log.e(TAG, "Lỗi: " + message);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout(SharedPreferences sharedPreferences) {
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();  // Xóa dữ liệu khi đăng xuất
            editor.apply();
            Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

}
