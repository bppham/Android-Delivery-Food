package com.example.ptitdelivery.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ptitdelivery.R;
import com.example.ptitdelivery.model.Chat.Message;
import com.example.ptitdelivery.model.User;
import com.example.ptitdelivery.network.SocketManager;
import com.example.ptitdelivery.utils.SharedPreferencesHelper;
import com.example.ptitdelivery.viewmodel.ChatViewModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Message> messageList;
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private FragmentActivity activity;
    private String chatId;

    public MessageAdapter(FragmentActivity activity, Context context, List<Message> messageList, String chatId) {
        this.activity = activity;
        this.context = context;
        this.messageList = messageList;
        this.chatId = chatId;
    }

    @Override
    public int getItemViewType(int position) {
//        User savedUser = SharedPreferencesHelper.getInstance(context).getCurrentUser();
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);

        if (messageList.get(position).getSender().getId().equals(id)) {
            return VIEW_TYPE_SENT; // Tin nhắn do chính mình gửi
        } else {
            return VIEW_TYPE_RECEIVED; // Tin nhắn người khác gửi
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_right, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_left, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof SentMessageViewHolder) {
            SentMessageViewHolder sentHolder = (SentMessageViewHolder) holder;
            sentHolder.bind(message);

            sentHolder.btnOption.setOnClickListener(v -> {
                showOptionsDialog(message);
            });

        } else if (holder instanceof ReceivedMessageViewHolder) {
            ReceivedMessageViewHolder receivedHolder = (ReceivedMessageViewHolder) holder;
            receivedHolder.bind(message);
        }
    }

    private void showOptionsDialog(Message message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn hành động");

        builder.setItems(new CharSequence[] {"Xóa"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Tùy chọn "Xóa"
                    onDeleteMessage(message);
                    break;
            }
        });

        builder.create().show();
    }

    private void onDeleteMessage(Message message) {
        ChatViewModel chatViewModel = new ViewModelProvider(activity).get(ChatViewModel.class);
        new AlertDialog.Builder(context)
                .setTitle("Xóa tin nhắn")
                .setMessage("Bạn có chắc chắn muốn xóa tin nhắn này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    chatViewModel.deleteMessage(message.getId(), chatId);
                    SocketManager.deleteMessage(message.getId());
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageView imgMessage;
        ImageButton btnOption;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            imgMessage = itemView.findViewById(R.id.imgMessage);
            btnOption = itemView.findViewById(R.id.btnOption);
        }

        void bind(Message message) {
            if (message != null) {
                if (message.getImage() != null && !message.getImage().getUrl().isEmpty()) {
                    imgMessage.setVisibility(View.VISIBLE);
                    tvMessage.setVisibility(View.GONE);
                    Glide.with(itemView.getContext()).load(message.getImage().getUrl()).into(imgMessage);
                } else if (message.getContent() != null) {
                    imgMessage.setVisibility(View.GONE);
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText(message.getContent());
                } else {
                    imgMessage.setVisibility(View.GONE);
                    tvMessage.setVisibility(View.GONE);
                }
            }
        }


    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageView imgMessage;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            imgMessage = itemView.findViewById(R.id.imgMessage);
        }

        void bind(Message message) {
            if (message.getImage() != null && !message.getImage().getUrl().isEmpty()) {
                imgMessage.setVisibility(View.VISIBLE);
                tvMessage.setVisibility(View.GONE);
                Glide.with(itemView.getContext()).load(message.getImage().getUrl()).into(imgMessage);
            } else if (message.getContent() != null) {
                imgMessage.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(message.getContent());
            } else {
                imgMessage.setVisibility(View.GONE);
                tvMessage.setVisibility(View.GONE);
            }
        }

    }
}