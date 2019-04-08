package com.driver.util;

/**
 * @author ZZD
 * @time 17/6/29 15:41
 */
public class UtilsLog {

    /**
     * 是否开启日志
     */
    public static boolean isTest = true;


    public static void d(String key, String value) {
        if (isTest) {
            android.util.Log.d(key, value);
        }
    }

    public static void i(String key, String value) {
        if (isTest) {
            android.util.Log.i(key, value);
        }
    }

    public static void e(String key, String value) {
        if (isTest) {
            android.util.Log.e(key, value);
        }
    }

    public static void w(String key, String value) {
        if (isTest) {
            android.util.Log.w(key, value);
        }
    }

    public static void w(String key, Throwable tr) {
        if (isTest) {
            android.util.Log.w(key, tr);
        }
    }

    public static void w(String key, String value, Throwable tr) {
        if (isTest) {
            android.util.Log.w(key, value, tr);
        }
    }

    public static void log(String tag, String info) {
        StackTraceElement[] ste = new Throwable().getStackTrace();
        int i = 1;
        if (isTest) {
            StackTraceElement s = ste[i];
            android.util.Log.e(tag, String.format("======[%s][%s][%s]=====%s", s.getClassName(),
                    s.getLineNumber(), s.getMethodName(), info));
        }
    }
}
