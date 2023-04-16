package com.cstdr.chatgpt.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.cstdr.chatgpt.MyApplication;

public class ClipboardUtil {

    private static ClipboardManager cm;

    public static void init(Context context) {
        if (cm == null) {
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
