package com.leowong.project.eyepetizer.utils

import android.text.TextUtils
import android.util.Log
import com.leowong.project.eyepetizer.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object LogUtils {
    private var isPrintLog = BuildConfig.LOG_DEBUG
    /**
     * It is used for json pretty print
     */
    private val JSON_INDENT = 2

    init {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.LOG_DEBUG
            }
        })
    }

    fun d(tag: String, msg: String) {
        if (isPrintLog) {
            Logger.log(Logger.DEBUG, tag, msg, null)
        }
    }

    fun d(msg: String) {
        if (isPrintLog) {
            Log.d("CWT", msg)
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
                    d(tag, message)
                    return
                }
                if (json.startsWith("[")) {
                    val jsonArray = JSONArray(json)
                    val message = jsonArray.toString(JSON_INDENT)
                    d(tag, message)
                    return
                }
                e(tag, "Invalid Json")
            } catch (e: JSONException) {
                e(tag, "Invalid Json")
            }


        }
    }

}
