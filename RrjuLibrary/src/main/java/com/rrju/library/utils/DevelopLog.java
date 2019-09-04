package com.rrju.library.utils;

/**
 * 自定义日志
 */
public final class DevelopLog {

    public static int d(String tag, String msg) {
        if (AppConfig.getInstance().ismDebuggable()) {
            return android.util.Log.d(tag, msg);
        } else {
            return 0;
        }
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (AppConfig.getInstance().ismDebuggable()) {
            return android.util.Log.d(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int i(String tag, String msg) {
        if (AppConfig.getInstance().ismDebuggable()) {
            return android.util.Log.i(tag, msg);
        } else {
            return 0;
        }
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (AppConfig.getInstance().ismDebuggable()) {
            return android.util.Log.i(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int w(String tag, String msg) {
        if (AppConfig.getInstance().ismDebuggable()) {
            return android.util.Log.w(tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (AppConfig.getInstance().ismDebuggable()) {
            return android.util.Log.w(tag, msg, tr);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (AppConfig.getInstance().ismDebuggable()) {
            return android.util.Log.e(tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (AppConfig.getInstance().ismDebuggable()) {
            return android.util.Log.e(tag, msg, tr);
        }
        return 0;
    }
}
