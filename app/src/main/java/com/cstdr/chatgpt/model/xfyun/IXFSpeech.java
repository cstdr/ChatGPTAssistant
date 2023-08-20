package com.cstdr.chatgpt.model.xfyun;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public interface IXFSpeech {

    void destroy();

    void pause();

    void stop();

    void startRecordByXF(RecognizerDialogListener listener);

    String getResult(RecognizerResult recognizerResult);

    void speechStartSaying(String question, SynthesizerListener listener);
}
