package com.cstdr.chatgpt.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMessage implements IJSONMessage {

    public JSONArray mJSONArray;

    private JSONMessage() {
        mJSONArray = new JSONArray();
    }

    public static JSONMessage getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final JSONMessage INSTANCE = new JSONMessage();
    }

    @Override
    public void addUserMessage(String question) {
        JSONObject message = new JSONObject();
        try {
            message.put(Constant.MESSAGES_KEY_ROLE, Constant.MESSAGES_VALUE_ROLE_USER);
            message.put(Constant.MESSAGES_KEY_CONTENT, question);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mJSONArray.put(message);
    }

    @Override
    public void addBotMessage(String question) {
        JSONObject message = new JSONObject();
        try {
            message.put(Constant.MESSAGES_KEY_ROLE, Constant.MESSAGES_VALUE_ROLE_ASSISTANT);
            message.put(Constant.MESSAGES_KEY_CONTENT, question);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mJSONArray.put(message);
    }

    @Override
    public void removeNotNeededMessage() {
        if (mJSONArray.length() > 8) {
            mJSONArray.remove(0);
        }
    }

    @Override
    public JSONArray getArray() {
        return mJSONArray;
    }
}
