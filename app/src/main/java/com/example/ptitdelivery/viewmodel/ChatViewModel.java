package com.example.ptitdelivery.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.ptitdelivery.model.ApiResponse;
import com.example.ptitdelivery.model.Chat.Chat;
import com.example.ptitdelivery.model.Chat.Message;
import com.example.ptitdelivery.model.Chat.MessageResponse;
import com.example.ptitdelivery.repositories.ChatRepository;
import com.example.ptitdelivery.utils.Resource;

import java.util.List;
import java.util.Map;
public class ChatViewModel extends ViewModel {
    private ChatRepository repository;

    public void init(String token) {
        repository = new ChatRepository(token);
    }

    private final MutableLiveData<Resource<String>> createChatResponse = new MutableLiveData<Resource<String>>();
    public LiveData<Resource<String>> getCreateChatResponse() {
        return createChatResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<Message>>> sendMessageResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<Message>>> getSendMessageResponse() {
        return sendMessageResponse;
    }
    private final MutableLiveData<Resource<List<Chat>>> allChatsResponse = new MutableLiveData<>();
    public LiveData<Resource<List<Chat>>> getAllChatsResponse() {
        return allChatsResponse;
    }
    private final MutableLiveData<Resource<MessageResponse>> allMessagesResponse = new MutableLiveData<>();
    public LiveData<Resource<MessageResponse>> getAllMessagesResponse() {
        return allMessagesResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> deleteChatResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getDeleteChatResponse() {
        return deleteChatResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> deleteMessageResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getDeleteMessageResponse() {
        return deleteMessageResponse;
    }
    public void createChat(String id, String storeId) {
        LiveData<Resource<String>> result = repository.createChat(id, storeId);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                createChatResponse.setValue(resource);
            }
        });
    }

    public void sendMessage(String chatId, Map<String, Object> data) {
        LiveData<Resource<ApiResponse<Message>>> result = repository.sendMessage(chatId, data);
        result.observeForever(new Observer<Resource<ApiResponse<Message>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Message>> resource) {
                sendMessageResponse.setValue(resource);
            }
        });
    }

    public void getAllChats() {
        LiveData<Resource<List<Chat>>> result = repository.getAllChats();
        result.observeForever(new Observer<Resource<List<Chat>>>() {
            @Override
            public void onChanged(Resource<List<Chat>> resource) {
                allChatsResponse.setValue(resource);
            }
        });
    }

    public void getAllMessages(String chatId) {
        LiveData<Resource<MessageResponse>> result = repository.getAllMessages(chatId);
        result.observeForever(new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                allMessagesResponse.setValue(resource);
            }
        });
    }

    public void deleteChat(String id) {
        LiveData<Resource<ApiResponse<String>>> result = repository.deleteChat(id);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                deleteChatResponse.setValue(resource);
            }
        });
    }

    public void deleteMessage(String messageId, String chatId) {
        LiveData<Resource<ApiResponse<String>>> result = repository.deleteMessage(messageId);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                deleteMessageResponse.setValue(resource);

                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    getAllMessages(chatId); // GỌI LẠI danh sách tin nhắn sau khi xóa thành công
                }
            }
        });
    }
}

