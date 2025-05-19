package com.example.ptitdelivery.fragments;

import static com.example.ptitdelivery.utils.Resource.Status.LOADING;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.activities.DetailMessageActivity;
import com.example.ptitdelivery.activities.OrderDetailActivity;
import com.example.ptitdelivery.activities.SeeRouteToCustomerActivity;
import com.example.ptitdelivery.activities.SeeRouteToStoreActivity;
import com.example.ptitdelivery.model.Chat.Chat;
import com.example.ptitdelivery.model.Order.Order;
import com.example.ptitdelivery.utils.ConvertString;
import com.example.ptitdelivery.utils.DialogHelper;
import com.example.ptitdelivery.utils.Resource;
import com.example.ptitdelivery.viewmodel.ChatViewModel;
import com.example.ptitdelivery.viewmodel.OngoingOrderViewModel;

public class OngoingOrderFragment extends Fragment {
    private static final String TAG = "OngoingOrderFragment";
    private OngoingOrderViewModel viewModel;
    private LinearLayout noOrderLayout;
    private LinearLayout hasOrderLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Order order;
    private ChatViewModel chatViewModel;
    // Step 1
    private CardView cvStep1;
    private TextView tvStoreName, tvStoreAddress, tvOrderStatus;
    private Button btnNextStep1, btnDetailOrderStep1, btnShowDirectionStep1, btnChatWithStore;
    // Step 2
    private CardView cvStep2;
    private TextView tvUserName, tvShippingAddress, tvUserPhoneNumber, tvPaymentMethod;
    private Button btnNextStep2, btnCallUser, btnDetailOrderStep2, btnShowDirectionStep2, btnChatWithClient;
    // Step 3
    private CardView cvStep3;
    private TextView tvOrderId;
    private Button btnNextStep3, btnDetailOrderStep3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_onging_order, container, false);

        // Ánh xá
        noOrderLayout = view.findViewById(R.id.layout_no_ongoing_order);
        hasOrderLayout = view.findViewById(R.id.layout_has_ongoing_order);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        // Step 1
        cvStep1 = view.findViewById(R.id.cv_ongoing_order_step_1);
        tvStoreName = view.findViewById(R.id.tv_ongoing_order_store_name);
        tvStoreAddress = view.findViewById(R.id.tv_ongoing_order_store_address);
        tvOrderStatus = view.findViewById(R.id.tv_ongoing_order_status);
        btnNextStep1 = view.findViewById(R.id.btnNextStep1);
        btnDetailOrderStep1 = view.findViewById(R.id.btnDetailOrderStep1);
        btnShowDirectionStep1 = view.findViewById(R.id.btnShowDirectionStep1);
        btnChatWithStore = view.findViewById(R.id.btnChatWithStore);
        // Step 2
        cvStep2 = view.findViewById(R.id.cv_ongoing_order_step_2);
        tvUserName = view.findViewById(R.id.tv_ongoing_order_user_name);
        tvShippingAddress = view.findViewById(R.id.tv_ongoing_order_shipping_address);
        tvUserPhoneNumber = view.findViewById(R.id.tv_ongoing_order_user_phonenumber);
        tvPaymentMethod = view.findViewById(R.id.tv_ongoing_order_payment_method);
        btnNextStep2 = view.findViewById(R.id.btnNextStep2);
        btnCallUser = view.findViewById(R.id.btnCallUser);
        btnDetailOrderStep2 = view.findViewById(R.id.btnDetailOrderStep2);
        btnShowDirectionStep2 = view.findViewById(R.id.btnShowDirectionStep2);
        btnChatWithClient = view.findViewById(R.id.btnChatWithClient);
        // Step 3
        cvStep3 = view.findViewById(R.id.cv_ongoing_order_step_3);
        tvOrderId = view.findViewById(R.id.tv_ongoing_order_id);
        btnNextStep3 = view.findViewById(R.id.btnNextStep3);
        btnDetailOrderStep3 = view.findViewById(R.id.btnDetailOrderStep3);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Log.d(TAG, "Stored Token: " + token);
        if (token == null) {
            Log.e(TAG, "Không tìm thấy token");
            return view;
        }
        viewModel = new ViewModelProvider(this).get(OngoingOrderViewModel.class);
        viewModel.init(token);
        swipeRefreshLayout.setRefreshing(true);
        viewModel.getTakenOrder();
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.init(token);

        chatViewModel.getCreateChatResponse().observe(getViewLifecycleOwner(), new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                Log.d(TAG, "Create chat response status: " + resource.getStatus());

                switch (resource.getStatus()) {
                    case LOADING:
                        Log.d(TAG, "Đang tạo chat...");
                        break;
                    case SUCCESS:
                        Log.d(TAG, "Tạo chat thành công.");
                        String chat = resource.getData();
                        if (chat != null) {
                            Intent intent = new Intent(getActivity(), DetailMessageActivity.class);
                            intent.putExtra("chatId", chat);
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "Chat rỗng.");
                        }
                        break;
                    case ERROR:
                        Log.e(TAG, "Lỗi khi tạo chat: " + resource.getMessage());
                        break;
                }
            }
        });

        observeViewModel();

        return view;
    }

    private void observeViewModel() {
        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                swipeRefreshLayout.setRefreshing(isLoading);
            }
        });

        viewModel.getTakenOrderLiveData().observe(getViewLifecycleOwner(), order -> {
            swipeRefreshLayout.setRefreshing(false);
            if (order != null) {
                Log.d(TAG, "Đã lấy được đơn hàng: " + order.getId());
                noOrderLayout.setVisibility(View.GONE);
                hasOrderLayout.setVisibility(View.VISIBLE);
                this.order = order;
                // Step 1
                tvStoreName.setText(order.getStore().getName());
                tvStoreAddress.setText(order.getStore().getAddress().getFull_address());
                String displayStatus = ConvertString.convertOrderStatus(order.getStatus());
                tvOrderStatus.setText(displayStatus);

                // Step 2
                tvUserName.setText(order.getCustomerName());
                tvShippingAddress.setText(order.getShipLocation().getAddress());
                tvUserPhoneNumber.setText(order.getCustomerPhonenumber());
                String displayPaymentMethod = ConvertString.convertPaymentMethod(order.getPaymentMethod());
                tvPaymentMethod.setText(displayPaymentMethod);

                // Action
                actionStep1();
                actionStep2();
                actionStep3();

                // Step 3
                tvOrderId.setText(order.getId());

                // Handle UI
                updateStepUI(order.getStatus());
            } else {
                // Không có đơn hàng
                noOrderLayout.setVisibility(View.VISIBLE);
                hasOrderLayout.setVisibility(View.GONE);
            }
        });

        viewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Log.e(TAG, "Lỗi: " + message);
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        viewModel.getUpdatedOrderLiveData().observe(getViewLifecycleOwner(), updatedOrder -> {
            swipeRefreshLayout.setRefreshing(false);
            if (updatedOrder != null) {
                this.order = updatedOrder;
                updateStepUI(updatedOrder.getStatus());
                Toast.makeText(getContext(), "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();

                // 👉 Nếu đơn đã hoàn thành, gọi lại getTakenOrder() để load đơn mới
                if ("done".equals(updatedOrder.getStatus())) {
                    noOrderLayout.setVisibility(View.VISIBLE);
                    hasOrderLayout.setVisibility(View.GONE);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        viewModel.getTakenOrder(); // Gọi lại đơn hàng tiếp theo
                    }, 1000); // Chờ 1s cho server cập nhật xong
                }
            }
        });

    }
    private void refreshData() {
        viewModel.getTakenOrder();
    }
    private void actionStep1(){
        btnNextStep1.setOnClickListener(v -> {
            if (order != null) {
                DialogHelper.showConfirmDialog(requireContext(), "Xác nhận đã lấy đơn tại cửa hàng?", () -> {
                    viewModel.updateOrderStatus(order.getId(), "delivering");
                });
            }
        });
        btnDetailOrderStep1.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });
        btnShowDirectionStep1.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SeeRouteToStoreActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });
        btnChatWithStore.setOnClickListener(view -> {
            chatViewModel.createChat(order.getStore().getOwner(), order.getStore().getId());
        });
    }
    private void actionStep2(){
        btnCallUser.setOnClickListener(v -> {
            if (order != null && order.getUser() != null) {
                String phoneNumber = order.getUser().getPhonenumber();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Không có số điện thoại để gọi", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Chưa có thông tin đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
        btnNextStep2.setOnClickListener(v -> {
            if (order != null) {
                DialogHelper.showConfirmDialog(requireContext(), "Xác nhận đã giao đơn cho khách?", () -> {
                    viewModel.updateOrderStatus(order.getId(), "delivered");
                });
            }
        });
        btnDetailOrderStep2.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });

        btnShowDirectionStep2.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SeeRouteToCustomerActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });
        btnChatWithClient.setOnClickListener(v -> {
            chatViewModel.createChat(order.getUser().getId(), null);
            Log.d(TAG, order.getUser().getId());
            Log.d(TAG, order.getUser().getName());
        });
    }
    private void actionStep3(){
        btnNextStep3.setOnClickListener(v -> {
            if (order != null) {
                DialogHelper.showConfirmDialog(requireContext(), "Xác nhận hoàn thành đơn hàng?", () -> {
                    viewModel.updateOrderStatus(order.getId(), "done");
                });
            }
        });
        btnDetailOrderStep3.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });
    }
    private void updateStepUI(String status) {
        if (status.equals("taken")) {
            enableAllChildren(cvStep1);
            disableAllChildren(cvStep2);
            disableAllChildren(cvStep3);
        } else if (status.equals("delivering")) {
            disableAllChildren(cvStep1);
            enableAllChildren(cvStep2);
            disableAllChildren(cvStep3);
        } else if (status.equals("delivered")) {
            disableAllChildren(cvStep1);
            disableAllChildren(cvStep2);
            enableAllChildren(cvStep3);
        }
    }
    private void disableAllChildren(ViewGroup viewGroup) {
        viewGroup.setAlpha(0.6f);  // Làm mờ luôn cả thẻ ngoài
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            child.setEnabled(false);
            child.setClickable(false);
            if (child instanceof ViewGroup) {
                disableAllChildren((ViewGroup) child);
            }
        }
    }
    private void enableAllChildren(ViewGroup viewGroup) {
        viewGroup.setAlpha(1.0f);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            child.setEnabled(true);
            child.setClickable(true);
            if (child instanceof ViewGroup) {
                enableAllChildren((ViewGroup) child);
            }
        }
    }

}
