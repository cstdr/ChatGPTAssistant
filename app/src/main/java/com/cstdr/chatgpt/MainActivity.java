package com.cstdr.chatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cstdr.chatgpt.adapter.ChatListAdapter;
import com.cstdr.chatgpt.bean.ChatMessage;
import com.cstdr.chatgpt.constant.Constant;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.TextToSpeechCallback;
import net.gotev.speech.ui.SpeechProgressView;

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
    private Button mBtnRecord;

    private List<ChatMessage> mChatMessageList;
    private ChatListAdapter mListAdapter;

    public OkHttpClient client = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
    private SpeechProgressView mSPVRecord;
    private LinearLayout mLlRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Speech.init(this, getPackageName());

        mContext = this;

        initView();
        initChatMessageList();
        initAdpater();

        initWelcomeContent();

    }

    private void initWelcomeContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String owner = Constant.OWNER_BOT;
                        String question = "欢迎和我聊天，我是蔡特鸡皮踢";

                        ChatMessage chatMessage = new ChatMessage(owner, question);
                        mChatMessageList.add(chatMessage);
                        mListAdapter.notifyDataSetChanged();
                        mRvChatList.smoothScrollToPosition(mListAdapter.getItemCount());

                        Speech.getInstance().say(question, new TextToSpeechCallback() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onCompleted() {
                                while (true) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    if (!Speech.getInstance().isSpeaking()) {
                                        String owner = Constant.OWNER_BOT;
                                        String question = "你可以让我讲一个冷笑话🤣";

                                        ChatMessage chatMessage = new ChatMessage(owner, question);
                                        mChatMessageList.add(chatMessage);
                                        mListAdapter.notifyDataSetChanged();
                                        mRvChatList.smoothScrollToPosition(mListAdapter.getItemCount());
                                        Speech.getInstance().say(question, new TextToSpeechCallback() {
                                            @Override
                                            public void onStart() {

                                            }

                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });

                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });

            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Speech.getInstance().shutdown();
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

        mLlRecord = findViewById(R.id.ll_record);
        mSPVRecord = findViewById(R.id.spv_record);
        mBtnRecord = findViewById(R.id.btn_record);
        mBtnRecord.setOnClickListener((v) -> {
            requestPermissions();
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            mLlRecord.setVisibility(View.VISIBLE);
            startRecord();
        }
    }

    private void startRecord() {
        try {
            Speech.getInstance().startListening(mSPVRecord, new SpeechDelegate() {
                @Override
                public void onStartOfSpeech() {

                }

                @Override
                public void onSpeechRmsChanged(float value) {

                }

                @Override
                public void onSpeechPartialResults(List<String> results) {

                }

                @Override
                public void onSpeechResult(String result) {
                    mLlRecord.setVisibility(View.GONE);
                    Log.d(TAG, "onSpeechResult: === " + result);
                    if (!TextUtils.isEmpty(result)) {
                        mEtQuestion.setText(result.trim());
                        mBtnSend.performClick();
                    }
                }
            });
        } catch (SpeechRecognitionNotAvailable e) {
            throw new RuntimeException(e);
        } catch (GoogleVoiceTypingDisabledException e) {
            throw new RuntimeException(e);
        }
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

                if (owner.equals(Constant.OWNER_BOT)) {
                    Speech.getInstance().say(question, new TextToSpeechCallback() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
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