package com.cstdr.chatgpt.model;

public interface IChatMessageData {

    int getSize();

    ChatMessage getChatMessage(int position);

    void addChatMessage(String owner, String question);

    String addWelcomeMessage();

    void removeLastChatMessage();

    boolean isBot(String owner);

}
