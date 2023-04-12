package com.cstdr.chatgpt.constant;

import okhttp3.MediaType;

public class Constant {

    // 2023年4月12日 by shamu
//    public static final String API = "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";
    public static final String API = "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";

    public static final String URL = "https://api.openai.com/v1/chat/completions";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_API_KEY = "Bearer " + API;
    public static final String MODEL = "model";
    public static final String MODEL_GPT35 = "gpt-3.5-turbo";

    public static final String MESSAGES = "messages";
    public static final String MESSAGES_KEY_ROLE = "role";
    public static final String MESSAGES_VALUE_ROLE_ASSISTANT = "assistant";
    public static final String MESSAGES_VALUE_ROLE_USER = "user";
    public static final String MESSAGES_KEY_CONTENT = "content";

    public static final String TEMPERATURE = "temperature";
    public static final double TEMPERATURE_LOW = 0;
    public static final double TEMPERATURE_MIDDLE = 0.5;

    public static final String OWNER_BOT_THINK = "OWNER_BOT_THINK";
    public static final String OWNER_BOT = "OWNER_BOT";
    public static final String OWNER_HUMAN = "OWNER_HUMAN";

    public static final String RESPONSE_CHOICES = "choices";
    public static final String RESPONSE_CHOICES_MESSAGE = "message";


}
