package com.derrick.journalapp.utilities;

import android.util.Log;

/**
 * @author derrick
 */
public class LogUtils {
    /**
     * Set to true to enable logging
     */
    private static final boolean IS_LOG_ENABLED = true;

    /**
     * To display debug message with tag when isDEBUG = true
     *
     * @param tag     LogTag
     * @param message Log message
     */
    public static void showDebugLog(String tag, String message) {
        if (IS_LOG_ENABLED)
            Log.d(tag, message + "");
    }

    /**
     * To display debug message with tag when isDEBUG = true
     *
     * @param tag     LogTag
     * @param message Log message
     */
    public static void showInformationLog(String tag, String message) {
        if (IS_LOG_ENABLED)
            Log.i(tag, message + "");
    }

    /**
     * To display debug message with tag when isDEBUG = true
     *
     * @param tag     LogTag
     * @param message Log message
     */
    public static void showErrorLog(String tag, String message) {
        if (IS_LOG_ENABLED)
            Log.e(tag, message + "");
    }

    /**
     * To display Exception message with "EXCEPTION" tag when isDEBUG = true
     *
     * @param exception Error message
     */
    public static void showException(Exception exception) {
        if (IS_LOG_ENABLED) {
            Log.e("EXCEPTION: ", exception.getMessage() + "");
            exception.printStackTrace();
        }
    }

    /**
     * To display debug message with tag when isDEBUG = true
     *
     * @param tag     LogTag
     * @param message Log message
     */
    public static void showLog(String tag, String message) {
        if (IS_LOG_ENABLED)
            Log.d(tag, message + "");
    }
}
