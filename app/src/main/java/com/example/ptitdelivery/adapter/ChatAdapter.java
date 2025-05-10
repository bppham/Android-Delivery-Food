package com.example.ptitdelivery.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.ptitdelivery.R;
import com.example.ptitdelivery.model.Chat.Chat;
import com.example.ptitdelivery.viewmodel.ChatViewModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private List<Chat> chatList;
    private OnChatClickListener onChatClickListener;
    private FragmentActivity fragment;
    private String shipper_id;
    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatAdapter(FragmentActivity fragment, Context context, List<Chat> chatList, String shipper_id, OnChatClickListener onChatClickListener) {
        this.fragment = fragment;
        this.context = context;
        this.chatList = chatList;
        this.shipper_id = shipper_id;
        this.onChatClickListener = onChatClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        if(chat.getStore() != null) {
            holder.tvUserName.setText(chat.getStore().getName());

            String userAvatarUrl = chat.getStore().getAvatar() != null ? chat.getStore().getAvatar().getUrl() : null;
            Glide.with(context)
                    .asBitmap()
                    .load(userAvatarUrl)
                    .override(60, 60)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.ivUserAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable roundedDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            roundedDrawable.setCornerRadius(6);
                            holder.ivUserAvatar.setImageDrawable(roundedDrawable);
                        }
                    });
        } else if (shipper_id.equals(chat.getUsers().get(0).getId())){

            holder.tvUserName.setText(chat.getUsers().get(1).getName());

            String userAvatarUrl = chat.getUsers().get(1).getAvatar() != null ? chat.getUsers().get(1).getAvatar().getUrl() : null;
            Glide.with(context)
                    .asBitmap()
                    .load(userAvatarUrl)
                    .override(60, 60)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.ivUserAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable roundedDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            roundedDrawable.setCornerRadius(6);
                            holder.ivUserAvatar.setImageDrawable(roundedDrawable);
                        }
                    });
        } else {
            holder.tvUserName.setText(chat.getUsers().get(0).getName());

            String userAvatarUrl = chat.getUsers().get(0).getAvatar() != null ? chat.getUsers().get(0).getAvatar().getUrl() : null;
            Glide.with(context)
                    .asBitmap()
                    .load(userAvatarUrl)
                    .override(60, 60)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.ivUserAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable roundedDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            roundedDrawable.setCornerRadius(6);
                            holder.ivUserAvatar.setImageDrawable(roundedDrawable);
                        }
                    });
        }

        if (chat.getLatestMessage() != null && chat.getLatestMessage().getContent() != null && !chat.getLatestMessage().getContent().isEmpty()) {
            holder.tvLatestMessage.setText(chat.getLatestMessage().getContent());
        } else {
            holder.tvLatestMessage.setText("");
        }

        Timestamp timestamp = chat.getUpdatedAt();
        Date date = new Date(timestamp.getTime());
        Calendar now = Calendar.getInstance();
        Calendar chatTime = Calendar.getInstance();
        chatTime.setTime(date);

        // Kiểm tra nếu cùng một ngày
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());

        String formattedDate;
        if (now.get(Calendar.YEAR) == chatTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == chatTime.get(Calendar.DAY_OF_YEAR)) {
            formattedDate = timeFormat.format(date); // Cùng ngày, hiển thị HH:mm
        } else {
            formattedDate = dateFormat.format(date); // Khác ngày, hiển thị dd/MM
        }

        holder.tvCreatedTime.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> {
            if (onChatClickListener != null) {
                onChatClickListener.onChatClick(chat);
            }
        });

        holder.btnOption.setOnClickListener(v -> {
            showOptionsDialog(chat);
        });
    }

    private void showOptionsDialog(Chat chat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn hành động");

        builder.setItems(new CharSequence[] {"Xóa"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Tùy chọn "Xóa"
                    onDeleteChat(chat);
                    break;
            }
        });

        builder.create().show();
    }

    private void onDeleteChat(Chat chat) {
        ChatViewModel chatViewModel = new ViewModelProvider(fragment).get(ChatViewModel.class);
        new AlertDialog.Builder(context)
                .setTitle("Xóa tin nhắn")
                .setMessage("Bạn có chắc chắn muốn xóa tin nhắn này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    chatViewModel.deleteChat(chat.getId());
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLatestMessage, tvCreatedTime;
        ImageView ivUserAvatar;
        ImageButton btnOption;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLatestMessage = itemView.findViewById(R.id.tvLatestMessage);
            tvCreatedTime = itemView.findViewById(R.id.tvCreatedTime);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            btnOption = itemView.findViewById(R.id.btnOption);
        }
    }
}
