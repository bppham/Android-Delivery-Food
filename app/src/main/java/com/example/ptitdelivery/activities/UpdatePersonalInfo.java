package com.example.ptitdelivery.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.model.Shipper.Shipper;
import com.example.ptitdelivery.model.Vehicle;
import com.example.ptitdelivery.viewmodel.ProfileViewModel;

public class UpdatePersonalInfo extends AppCompatActivity {
    private static final String TAG = "UpdatePersonalInfo";
    private EditText edtPhonenumber, edtVehicleName, edtVehicleNumber;
    private RadioGroup radioGroupGender;
    private RadioButton radioMale, radioFemale, radioOther;
    private Button btnConfirm;
    private Toolbar toolbar;
    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_personal_info);

        edtPhonenumber = findViewById(R.id.edt_update_phonenumber);
        edtVehicleName = findViewById(R.id.edt_update_vehicle_name);
        edtVehicleNumber = findViewById(R.id.edt_update_vehicle_number);
        radioGroupGender = findViewById(R.id.radioGroupUpdateGender);
        toolbar = findViewById(R.id.toolbar_update_profile);
        btnConfirm = findViewById(R.id.btn_update_copnfirm);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioOther = findViewById(R.id.radioOther);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        Shipper shipper = (Shipper) getIntent().getSerializableExtra("shipper");
        if (shipper != null) {
            showInformatioin(shipper);
        } else {
            Log.d(TAG, "onCreate: shipper is null");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Log.d(TAG, "Stored Token: " + token);
        if (token == null) {
            Log.e(TAG, "Không tìm thấy token");
        }
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.init(token);

        action();

    }
    private void showInformatioin(Shipper shipper){
        edtPhonenumber.setText(shipper.getPhonenumber());
        edtVehicleName.setText(shipper.getVehicle().getName());
        edtVehicleNumber.setText(shipper.getVehicle().getNumber());

        String gender = shipper.getGender();
        if (gender != null) {
            switch (gender.toLowerCase()) {
                case "male":
                    radioMale.setChecked(true);
                    break;
                case "female":
                    radioFemale.setChecked(true);
                    break;
                case "other":
                    radioOther.setChecked(true);
                    break;
                default:
                    break;
            }
        }
    }

    private void action() {
        btnConfirm.setOnClickListener(v -> {
            // Lấy dữ liệu mới từ form
            String newPhoneNumber = edtPhonenumber.getText().toString().trim();
            String newVehicleName = edtVehicleName.getText().toString().trim();
            String newVehicleNumber = edtVehicleNumber.getText().toString().trim();

            // Lấy giới tính mới
            String newGender = null;
            int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
            if (selectedGenderId == R.id.radioMale) {
                newGender = "male";
            } else if (selectedGenderId == R.id.radioFemale) {
                newGender = "female";
            } else if (selectedGenderId == R.id.radioOther) {
                newGender = "other";
            }

            // Tạo object shipper mới để gửi lên server
            Shipper updatedShipper = new Shipper();
            updatedShipper.setPhonenumber(newPhoneNumber);
            updatedShipper.setGender(newGender);

            Vehicle vehicle = new Vehicle();
            vehicle.setName(newVehicleName);
            vehicle.setNumber(newVehicleNumber);
            updatedShipper.setVehicle(vehicle);

            // Gọi ViewModel update
            viewModel.updateShipper(updatedShipper);

            String id = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).getString("id", null);
            if (id != null) {
                viewModel.getShipper(id); // Gọi lại để làm mới dữ liệu
            }

            // Có thể show loading hoặc quay về màn trước, tùy bạn
            Toast.makeText(this, "Đang cập nhật thông tin...", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}