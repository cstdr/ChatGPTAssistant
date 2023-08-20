package com.cstdr.chatgpt.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.cstdr.chatgpt.controller.MyApplication;

public class ClipboardUtil {

    private static ClipboardManager cm;

    public static void init() {
        if (cm == null) {
            Context context = MyApplication.getContext();
            cm = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        }
    }

    public static boolean copy(String text) {
        if (cm != null) {
            ClipData data = ClipData.newPlainText("bot", text);
            cm.setPrimaryClip(data);
            return true;
        }
        return false;
    }
}
