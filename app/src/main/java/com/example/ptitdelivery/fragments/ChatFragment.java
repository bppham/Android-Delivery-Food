package com.example.ptitdelivery.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.activities.DetailMessageActivity;
import com.example.ptitdelivery.adapter.ChatAdapter;
import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.Chat.Chat;
import com.example.ptitdelivery.network.SocketManager;
import com.example.ptitdelivery.utils.Resource;
import com.example.ptitdelivery.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatViewModel chatViewModel;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;
    private String shipper_id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        chatRecyclerView = view.findViewById(R.id.messageRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        shipper_id = sharedPreferences.getString("id", null);
        SocketManager.connectSocket(requireContext());
        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        setupUserChat();

        chatViewModel.getDeleteChatResponse().observe(getViewLifecycleOwner(), new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        setupUserChat();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        return view;
    }

    private void setupUserChat() {
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(requireActivity(),getContext(), chatList, shipper_id, chat -> {
            Intent intent = new Intent(requireContext(), DetailMessageActivity.class);
            intent.putExtra("chatId", chat.getId());
            startActivity(intent);
        });
        chatRecyclerView.setAdapter(chatAdapter);

        chatViewModel.getAllChatsResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<Chat>>>() {
            @Override
            public void onChanged(Resource<List<Chat>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        chatList.clear();
                        chatList.addAll(resource.getData());
                        Log.d("setupUserChat", "Số lượng chat nhận được: " + resource.getData().size());
                        for (Chat chat : resource.getData()) {
                            Log.d("setupUserChat", "Chat ID: " + chat.getId());
                        }
                        chatAdapter.notifyDataSetChanged();
                        Log.d("ChatRepository", "getAllChats: " + resource.getData());
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("ChatRepository", "getAllChats error: " + resource.getData());
                        break;
                }
            }
        });

        chatViewModel.getAllChats();
    }

    private void refreshData() {
        chatViewModel.getAllChats();
    }
}