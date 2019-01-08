package com.leo.android.videoplayer.utils

import android.util.Log
import com.leo.android.videoplayer.BuildConfig


object LogUtils {
    private var isPrintLog = BuildConfig.LOG_DEBUG
    private var TAG: String = "LEO"


    fun d(tag: String, msg: String) {
        if (isPrintLog) {
            Log.d(tag, msg)
        }
    }


    fun d(msg: String) {
        if (isPrintLog) {
            Log.d(TAG, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (isPrintLog) {
            Log.i(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (isPrintLog) {
            Log.e(tag, msg)
        }
    }

    fun e(msg: String) {
        if (isPrintLog) {
            Log.e(TAG, msg)
        }
    }

    fun v(tag: String, msg: String) {
        if (isPrintLog) {
            Log.v(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (isPrintLog) {
            Log.w(tag, msg)
        }
    }

    fun w(msg: String) {
        if (isPrintLog) {
            Log.w(TAG, msg)
        }
    }


}
