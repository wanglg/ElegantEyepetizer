package com.leowong.project.eyepetizer.utils

import android.text.TextUtils
import android.util.Log
import com.leowong.project.eyepetizer.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object LogUtils {
    private var isPrintLog = BuildConfig.LOG_DEBUG
    private var TAG: String = "LEO"
    /**
     * It is used for json pretty print
     */
    private val JSON_INDENT = 2

    init {
        Logger.addLogAdapter(object : AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag(TAG).build()) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.LOG_DEBUG
            }
        })
    }

    fun d(tag: String, msg: String) {
        if (isPrintLog) {
            Log.d(tag, msg)
        }
    }

    fun log(priority: Int, tag: String, msg: String) {
        if (isPrintLog) {
            Logger.log(priority, tag, msg, null)
        }
    }

    fun log(priority: Int, msg: String) {
        log(priority, TAG, msg)
    }

    fun log(msg: String) {
        log(Logger.DEBUG, TAG, msg)
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
    fun w( msg: String) {
        if (isPrintLog) {
            Log.w(TAG, msg)
        }
    }
    fun json(json: String) {
        Logger.json(json)
    }

    fun json(tag: String, str: String) {
        if (isPrintLog) {
            if (TextUtils.isEmpty(str)) {
                d(tag, "Empty/Null json content")
                return
            }
            try {
                val json = str.trim()
                if (json.startsWith("{")) {
                    val jsonObject = JSONObject(json)
                    val message = jsonObject.toString(JSON_INDENT)
                    log(Logger.DEBUG, tag, message)
                    return
                }
                if (json.startsWith("[")) {
                    val jsonArray = JSONArray(json)
                    val message = jsonArray.toString(JSON_INDENT)
                    log(Logger.DEBUG, tag, message)
                    return
                }
                e(tag, "Invalid Json")
            } catch (e: JSONException) {
                e(tag, "Invalid Json")
            }


        }
    }

}
