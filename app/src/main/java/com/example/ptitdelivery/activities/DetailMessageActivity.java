package com.example.ptitdelivery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.ptitdelivery.R;
import com.example.ptitdelivery.adapter.MessageAdapter;
import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.Chat.Chat;
import com.example.ptitdelivery.model.Chat.Message;
import com.example.ptitdelivery.model.Chat.MessageResponse;
import com.example.ptitdelivery.model.Image;
import com.example.ptitdelivery.network.SocketManager;
import com.example.ptitdelivery.utils.Resource;
import com.example.ptitdelivery.viewmodel.ChatViewModel;
import com.example.ptitdelivery.viewmodel.NotificationViewModel;
import com.example.ptitdelivery.viewmodel.UploadViewModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DetailMessageActivity extends AppCompatActivity {
    private static final String TAG = "DetailMessageActivity";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatViewModel chatViewModel;
    private UploadViewModel uploadViewModel;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private RecyclerView chatRecyclerView;
    private EditText etMessageInput;
    private ImageView sendMessage, ivUserAvatar, sendImage;
    private TextView tvUserName, tvTime;
    private String chatId;
    private String shipper_id;
    private static final int PICK_IMAGES_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_message);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        //tvTime = findViewById(R.id.tvTime);
        etMessageInput = findViewById(R.id.etMessageInput);
        sendMessage = findViewById(R.id.sendMessage);
        sendImage = findViewById(R.id.sendImage);

        chatId = getIntent().getStringExtra("chatId") != null ? getIntent().getStringExtra("chatId") : "";
        Log.d("DetailMessageActivity", "Chat ID: " + chatId);
        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        shipper_id = sharedPreferences.getString("id", null);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupAllMessages();

        sendImage.setOnClickListener(view -> openImagePicker());
        SocketManager.joinChat(chatId);

        sendMessage.setOnClickListener(v -> {
            String messageContent = etMessageInput.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                try {
                    Map<String, Object> data = new HashMap<>();
                    data.put("content", messageContent);
                    chatViewModel.sendMessage(chatId, data);

                    JSONObject dataObject = new JSONObject();
                    dataObject.put("content", messageContent);

                    JSONObject sendObject = new JSONObject();
                    sendObject.put("id", chatId);
                    sendObject.put("data", dataObject);

                    SocketManager.sendMessage(sendObject);

                    etMessageInput.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        SocketManager.setOnMessageReceivedListener(args  -> runOnUiThread(() -> {
//            Log.d("DetailMessageActivity", "onMessageReceived");
//            setupAllMessages();
//        }));

        SocketManager.setOnMessageDeletedListener(args  -> runOnUiThread(() -> {
            setupAllMessages();
        }));

        chatViewModel.getSendMessageResponse().observe(this, new Observer<Resource<ApiResponse<Message>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Message>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
//                        messageList.add(resource.getData().getData());
//                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        setupAllMessages();
                        chatRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                            }
                        });
                        break;
                    case ERROR:
                        break;
                }
            }
        });

        uploadViewModel.getUploadImagesResponse().observe(this, new Observer<Resource<List<Image>>>() {
            @Override
            public void onChanged(Resource<List<Image>> resource) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        List<Image> uploadedImageUrls = resource.getData();
                        if (uploadedImageUrls != null && !uploadedImageUrls.isEmpty()) {
                            Image image = uploadedImageUrls.get(0);

                            // Gửi dữ liệu qua ViewModel
                            Map<String, Object> data = new HashMap<>();
                            data.put("content", "");
                            data.put("image", image);
                            chatViewModel.sendMessage(chatId, data);

                            // Gửi dữ liệu qua Socket
                            try {
                                JSONObject dataObject = new JSONObject();
                                dataObject.put("content", "");
                                dataObject.put("image", new JSONObject(new Gson().toJson(image))); // nếu image là object

                                JSONObject sendObject = new JSONObject();
                                sendObject.put("id", chatId);
                                sendObject.put("data", dataObject);

                                SocketManager.sendMessage(sendObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ERROR:
                        // handle error
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                List<Uri> selectedImageUris = new ArrayList<>();
                if (!selectedImageUris.contains(uri)) {
                    selectedImageUris.add(uri);
                }
                uploadViewModel.uploadImages(selectedImageUris, this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.leaveChat(chatId);
    }

    private void setupAllMessages() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, this,messageList, chatId);
        chatRecyclerView.setAdapter(messageAdapter);

        chatViewModel.getAllMessagesResponse().observe(this, new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        messageList.clear();
                        messageList.addAll(resource.getData().getMessages());

                        Chat chat = resource.getData().getChat();

                        if(chat.getStore() != null) {
                            tvUserName.setText(chat.getStore().getName());

                            String avatarUrl = chat.getStore().getAvatar() != null
                                    ? chat.getStore().getAvatar().getUrl()
                                    : null;
                            loadRoundedImage(avatarUrl, ivUserAvatar);
                        } else {
                            if(shipper_id.equals(chat.getUsers().get(0).getId())){
                                tvUserName.setText(chat.getUsers().get(1).getName());

                                String avatarUrl = chat.getUsers().get(1).getAvatar() != null
                                        ? resource.getData().getChat().getUsers().get(1).getAvatar().getUrl()
                                        : null;
                                loadRoundedImage(avatarUrl, ivUserAvatar);
                            } else {
                                tvUserName.setText(chat.getUsers().get(0).getName());

                                String avatarUrl = chat.getUsers().get(0).getAvatar() != null
                                        ? resource.getData().getChat().getUsers().get(0).getAvatar().getUrl()
                                        : null;
                                loadRoundedImage(avatarUrl, ivUserAvatar);
                            }
                        }

//                        long timeDiff = System.currentTimeMillis() - resource.getData().getChat().getLatestMessage().getUpdatedAt().getTime();
//                        tvTime.setText(formatTimeAgo(timeDiff));

                        messageAdapter.notifyDataSetChanged();

                        chatRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if (messageList.size() > 0) {
                                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
                                }

                                // Remove listener để không bị gọi lại nhiều lần
                                chatRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });

                        break;
                    case ERROR:
                        Log.d("ChatFragment", "getAllMessagesResponse Error: " + resource.getMessage());
                        break;
                }
            }
        });

        chatViewModel.getAllMessages(chatId);
    }

    private void refreshData() {
        chatViewModel.getAllMessages(chatId);
    }

    private String formatTimeAgo(long timeDiff) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

        if (seconds < 60) return seconds + " giây trước";
        if (minutes < 60) return minutes + " phút trước";
        if (hours < 24) return hours + " giờ trước";
        return days + " ngày trước";
    }

    private void loadRoundedImage(String url, ImageView imageView) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .override(50, 50)
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        imageView.setImageDrawable(roundedDrawable);
                    }
                });
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(Intent.createChooser(intent, "Chọn 1 ảnh"), PICK_IMAGES_REQUEST);
    }
    public void goBack(View view) {
        finish();
    }

}