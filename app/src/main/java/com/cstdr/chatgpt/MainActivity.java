package com.cstdr.chatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cstdr.chatgpt.adapter.ChatListAdapter;
import com.cstdr.chatgpt.bean.ChatMessage;
import com.cstdr.chatgpt.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    private RecyclerView mRvChatList;
    private EditText mEtQuestion;
    private Button mBtnSend;
    private List<ChatMessage> mChatMessageList;
    private ChatListAdapter mListAdapter;

    public OkHttpClient client = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        initView();
        initChatMessageList();
        initAdpater();

        addChatMessage(Constant.OWNER_BOT, "欢迎和我聊天，我是蔡特鸡皮踢");
        addChatMessage(Constant.OWNER_BOT, "你可以让我讲个冷笑话hhh🤣");
    }

    private void initChatMessageList() {
        mChatMessageList = new ArrayList<ChatMessage>();
    }

    private void initAdpater() {
        mListAdapter = new ChatListAdapter(mChatMessageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        // 从底部加入聊天消息
        linearLayoutManager.setStackFromEnd(true);
        mRvChatList.setLayoutManager(linearLayoutManager);
        mRvChatList.setAdapter(mListAdapter);
    }

    private void initView() {
        mEtQuestion = findViewById(R.id.et_question);
        mBtnSend = findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener((v) -> {
            sendQuestion();
        });

        mRvChatList = findViewById(R.id.rv_chatlist);
    }

    private void sendQuestion() {
        String question = mEtQuestion.getText().toString().trim();
        if (TextUtils.isEmpty(question)) {
            Toast.makeText(this, "请先输入你的问题", Toast.LENGTH_SHORT).show();
            return;
        }

        mEtQuestion.setText("");

        // 发送文字到List里
        addChatMessage(Constant.OWNER_HUMAN, question);

        addChatMessage(Constant.OWNER_BOT_THINK, "正在思考中...");

        // TODO 发送文字到API接口
        sendQuestionToAPI(question);
    }

    private void addChatMessage(String owner, String question) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChatMessage chatMessage = new ChatMessage(owner, question);
                mChatMessageList.add(chatMessage);
                mListAdapter.notifyDataSetChanged();
                mRvChatList.smoothScrollToPosition(mListAdapter.getItemCount());
            }
        });
    }

    private void removeLastChatMessage() {
        if (mChatMessageList.size() > 0) {
            mChatMessageList.remove(mChatMessageList.size() - 1);
        }
    }

    private void sendQuestionToAPI(String question) {

        // JSONObject
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(Constant.MODEL, Constant.MODEL_GPT35);
            jsonBody.put(Constant.TEMPERATURE, Constant.TEMPERATURE_MIDDLE);

            // put message
            JSONObject message = new JSONObject();
            message.put(Constant.MESSAGES_KEY_ROLE, Constant.MESSAGES_VALUE_ROLE_USER);
            message.put(Constant.MESSAGES_KEY_CONTENT, question);

            JSONArray messagesArray = new JSONArray();
            messagesArray.put(message);

            jsonBody.put(Constant.MESSAGES, messagesArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Request
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), Constant.JSON);
        Request request = new Request.Builder().url(Constant.URL).header(Constant.AUTHORIZATION, Constant.AUTHORIZATION_API_KEY).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                removeLastChatMessage();
                addChatMessage(Constant.OWNER_BOT, "出错了，错误信息是：" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                removeLastChatMessage();

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        Log.d(TAG, "onResponse: ===" + response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.RESPONSE_CHOICES);
                        JSONObject message = jsonArray.getJSONObject(0).getJSONObject(Constant.RESPONSE_CHOICES_MESSAGE);
                        String content = message.getString(Constant.MESSAGES_KEY_CONTENT);

                        addChatMessage(Constant.OWNER_BOT, content.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                } else {
                    addChatMessage(Constant.OWNER_BOT, "出错了，错误信息是：" + response.body().string());
                }

            }
        });
    }
}