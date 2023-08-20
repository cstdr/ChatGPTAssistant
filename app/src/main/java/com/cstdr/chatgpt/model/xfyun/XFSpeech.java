package com.cstdr.chatgpt.model.xfyun;

import android.content.Context;
import android.util.Log;

import com.cstdr.chatgpt.controller.MyApplication;
import com.cstdr.chatgpt.model.API;
import com.cstdr.chatgpt.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 讯飞语音组件
 */
public class XFSpeech implements IXFSpeech {

    private static final String TAG = XFSpeech.class.getSimpleName();

    // ===============科大讯飞语音转写相关===================

    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private String resultType = "json";
    private String language = "zh_cn";
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    // ===============科大讯飞语音合成相关===================
    /**
     * 可以去官网找自己喜欢的声音
     * https://console.xfyun.cn/services/tts
     */
    private String voicer = "aisxping";
    private SpeechSynthesizer mTts;
    private File pcmFile;

    public XFSpeech(Context context, InitListener mInitListener) {
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(MyApplication.getContext(), SpeechConstant.APPID + "=" + API.XF_SPEECH);

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
//        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        mTts = SpeechSynthesizer.createSynthesizer(context, mInitListener);
        mIatDialog = new RecognizerDialog(context, mInitListener);

    }

    /**
     * 语音合成参数设置
     *
     * @return
     */
    private void setSpeechParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 支持实时音频返回，仅在 synthesizeToUri 条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, MyApplication.getContext().getExternalFilesDir("msc").getAbsolutePath() + "/tts.pcm");
    }

    @Override
    public void speechStartSaying(String question, SynthesizerListener listener) {
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }

        pcmFile = new File(MyApplication.getContext().getExternalCacheDir().getAbsolutePath(), "tts_pcmFile.pcm");
        pcmFile.delete();

        setSpeechParam();

        int code = mTts.startSpeaking(question, listener);
        if (code != ErrorCode.SUCCESS) {
        }
    }


    /**
     * 语音识别参数设置
     *
     * @return
     */
    public void setRecognizerParam() {
        // 清空参数
        mIatDialog.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, resultType);

        if (language.equals("zh_cn")) {
            String lag = "mandarin";
            // 设置语言
            Log.e(TAG, "language = " + language);
            mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIatDialog.setParameter(SpeechConstant.ACCENT, lag);
        } else {
            mIatDialog.setParameter(SpeechConstant.LANGUAGE, language);
        }

        //此处用于设置dialog中不显示错误码信息
        //mIatDialog.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIatDialog.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav.
        mIatDialog.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIatDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, MyApplication.getContext().getExternalFilesDir("msc").getAbsolutePath() + "/iat.wav");
    }

    @Override
    public void destroy() {
        if (mIatDialog != null) {
            // 退出时释放连接
            mIatDialog.cancel();
            mIatDialog.destroy();
        }
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    @Override
    public void pause() {
        mTts.pauseSpeaking();
    }

    @Override
    public void stop() {
        mTts.stopSpeaking();
    }

    @Override
    public void startRecordByXF(RecognizerDialogListener listener) {
        if (mIatDialog.isShowing()) {
            return;
        }
        setRecognizerParam();

        // 显示听写对话框
        mIatDialog.setListener(listener);
        mIatDialog.show();
    }

    @Override
    public String getResult(RecognizerResult recognizerResult) {
        String text = JsonParser.parseIatResult(recognizerResult.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        return resultBuffer.toString();
    }
}
