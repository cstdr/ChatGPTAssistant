package com.cstdr.chatgpt.model;

import org.json.JSONArray;

public interface IJSONMessage {

    void addUserMessage(String question);

    void addBotMessage(String question);

    void removeNotNeededMessage();

    JSONArray getArray();
}
