package com.leowong.project.client.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.leowong.project.client.AppApplication
import com.leowong.project.client.GlobalConstants


/**
 * SharedPreferences 工具类
 * Created by caowt on 2017/12/28 0028.
 */

class SharedPreferencesUtils private constructor() {
    private val preferences: SharedPreferences

    init {
        preferences = AppApplication.instance!!.getSharedPreferences(GlobalConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    /**
     * 保存数据
     */
    @SuppressLint("CommitPrefEdits")
    fun put(key: String?, obj: Any) {
        // key 不为null时再存入，否则不存储
        val editor: SharedPreferences.Editor?
        if (key != null) {
            editor = preferences.edit()
            if (obj is Int) {
                editor!!.putInt(key, obj)
            } else if (obj is Long) {
                editor!!.putLong(key, obj)
            } else if (obj is Boolean) {
                editor!!.putBoolean(key, obj)
            } else if (obj is Float) {
                editor!!.putFloat(key, obj)
            } else if (obj is Set<*>) {
                editor!!.putStringSet(key, obj as Set<String>)
            } else if (obj is String) {
                editor!!.putString(key, obj.toString())
            }
            editor!!.apply()
        }
    }

    /**
     * 根据key和类型取出数据
     */
    fun getValue(key: String, type: Int): Any? {
        when (type) {
            GlobalConstants.INTEGER -> return preferences.getInt(key, -1)
            GlobalConstants.FLOAT -> return preferences.getFloat(key, -1f)
            GlobalConstants.BOOLEAN -> return preferences.getBoolean(key, false)
            GlobalConstants.BOOLEAN_TRUE -> return preferences.getBoolean(key, true)
            GlobalConstants.LONG -> return preferences.getLong(key, -1L)
            GlobalConstants.STRING -> return preferences.getString(key, "")
            GlobalConstants.STRING_SET -> return preferences.getStringSet(key, null)
            else -> return null
        }
    }

    internal fun clear() {
        preferences.edit().clear().apply()
    }

    companion object {
        private var ourInstance: SharedPreferencesUtils? = null

        val instance: SharedPreferencesUtils
            get() {
                synchronized(SharedPreferencesUtils::class.java) {
                    if (ourInstance == null) {
                        ourInstance = SharedPreferencesUtils()
                    }
                }
                return ourInstance!!
            }
    }

}
