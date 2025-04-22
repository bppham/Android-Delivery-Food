package com.example.ptitdelivery.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.adapter.HistoryOrdersAdapter;
import com.example.ptitdelivery.viewmodel.DeliveredOrdersViewModel;

import java.util.ArrayList;

public class SumaryFragment extends Fragment {
    private static final String TAG = "SummaryOrderFragment";
    private DeliveredOrdersViewModel viewModel;
    private GridView gvOrders;
    private ProgressBar progressBar;
    private Button btnNext, btnPrev;
    private TextView tvPage;
    private HistoryOrdersAdapter adapter;
    private final int LIMIT = 10;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_sumary, container, false);
        gvOrders = view.findViewById(R.id.gv_history_orders);
        progressBar = view.findViewById(R.id.progressBarHistoryOrders);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrev = view.findViewById(R.id.btnPrev);
        tvPage = view.findViewById(R.id.tvPageNumber);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Log.d(TAG, "Stored Token: " + token);
        if (token == null) {
            Log.e(TAG, "Không tìm thấy token");
            return view;
        }

        viewModel = new ViewModelProvider(this).get(DeliveredOrdersViewModel.class);
        viewModel.init(token);
        // Tạo adapter một lần
        adapter = new HistoryOrdersAdapter(requireContext(), new ArrayList<>());
        gvOrders.setAdapter(adapter);

        // Gọi API lấy đơn hàng đã giao (page đầu tiên)
        Log.d(TAG, "Fetching delivered orders for page 1");
        viewModel.fetchDeliveredOrders(1, LIMIT);

        // Quan sát dữ liệu đơn hàng
        viewModel.getDeliveredOrders().observe(getViewLifecycleOwner(), orders -> {
            Log.d("SumaryFragment", "Có " + orders.size() + " đơn hàng");
            adapter.setOrders(orders);
        });

        // Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        // Thông báo lỗi
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });

        // Tổng số trang
        viewModel.getTotalPages().observe(getViewLifecycleOwner(), totalPages -> {
            updateButtonState();
        });

        // Trang hiện tại
        viewModel.getCurrentPage().observe(getViewLifecycleOwner(), page -> {
            tvPage.setText("Trang: " + page);
            updateButtonState();
        });

        // Phân trang
        btnNext.setOnClickListener(v -> viewModel.nextPage(LIMIT));
        btnPrev.setOnClickListener(v -> viewModel.previousPage(LIMIT));

        return view;
    }
    private void updateButtonState() {
        Integer page = viewModel.getCurrentPage().getValue();
        Integer totalPages = viewModel.getTotalPages().getValue();

        btnPrev.setEnabled(page != null && page > 1);
        btnNext.setEnabled(page != null && totalPages != null && page < totalPages);
    }
}
