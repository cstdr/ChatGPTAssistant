package com.cstdr.chatgpt.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageData implements IChatMessageData {

    private List<ChatMessage> mChatMessageList;

    private volatile static ChatMessageData mInstance;

    private ChatMessageData() {
        mChatMessageList = new ArrayList<>();
    }

    public static ChatMessageData getInstance() {
        if (mInstance == null) {
            synchronized (ChatMessageData.class) {
                if (mInstance == null) {
                    mInstance = new ChatMessageData();
                }
            }
        }
        return mInstance;
    }

    @Override
    public int getSize() {
        return mChatMessageList.size();
    }

    @Override
    public ChatMessage getChatMessage(@NonNull int position) {
        return mChatMessageList.get(position);
    }

    @Override
    public void addChatMessage(@NonNull String owner, @NonNull String question) {
        ChatMessage chatMessage = new ChatMessage(owner, question);
        mChatMessageList.add(chatMessage);
    }

    @Override
    public String addWelcomeMessage() {
        String owner = Constant.OWNER_BOT;
        String question = "欢迎和我聊天，我是蔡特鸡皮踢";
        ChatMessage chatMessage = new ChatMessage(owner, question);
        mChatMessageList.add(chatMessage);
        return question;
    }

    @Override
    public void removeLastChatMessage() {
        if (mChatMessageList.size() > 0) {
            mChatMessageList.remove(mChatMessageList.size() - 1);
        }
    }

    @Override
    public boolean isBot(@NonNull String owner) {
        if (owner.equals(Constant.OWNER_BOT)) {
            return true;
        }
        return false;
    }
}
