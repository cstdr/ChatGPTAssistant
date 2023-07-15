package com.cstdr.chatgpt.constant;

import okhttp3.MediaType;

public class Constant {

    // 这里需要替换成你自己的Key
    // https://platform.openai.com/account/api-keys
    public static final String API = OpenAI.API_KEY;
    /**
     * 20230715 服务器维护不可用
     * To use our hosted ChatGPT API, you can use the following steps:
     * 要使用我们托管的 ChatGPT API，您可以使用以下步骤：
     * Join our Discord server.
     * 加入我们的服务器。
     * Get your API key from the #Bot channel by sending /key command.
     * 通过发送 /key 命令从 #Bot 通道获取 API 密钥。
     * Use the API Key in your requests to the following endpoints.
     */
//    public static final String API = "pk-YVVWnOckxnJxxWacYGhPJMLLFPhNpTVLCFTmcrAeOrrpHWCL";

    /**
     * https://github.com/PawanOsman/ChatGPT#chat-completion-chatgpt
     */
//    public static final String URL = "https://api.pawan.krd/v1/chat/completions";
    public static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public static final String PROXY_USER_NAME = "cstdingran";
    public static final String PROXY_PASSWORD = "wo2sea02";
    public static final String PROXY_HOST_NAME = "61.171.90.153";
    public static final int PROXY_PORT = 16816;

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
