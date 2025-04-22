package com.example.ptitdelivery.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.adapter.NewOrderAdapter;
import com.example.ptitdelivery.viewmodel.NewOrdersViewModel;

public class NewOrdersFragment extends Fragment {
    private static final String TAG = "NewOrdersFragment";
    private NewOrdersViewModel viewModel;
    private ListView lvOrders;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        lvOrders = view.findViewById(R.id.lvOrders);
        progressBar = view.findViewById(R.id.progressBar);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Log.d(TAG, "Stored Token: " + token);
        if (token == null) {
            Log.e(TAG, "Không tìm thấy token");
            return view;
        }

        viewModel = new ViewModelProvider(this).get(NewOrdersViewModel.class);
        viewModel.init(token); // Truyền token khi init ViewModel
        viewModel.fetchOrders();

        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            NewOrderAdapter adapter = new NewOrderAdapter(requireContext(), orders, this);
            lvOrders.setAdapter(adapter);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
