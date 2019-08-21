package com.leo.android.log;

import android.content.Context;

import com.leo.android.log.core.ILog;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * @Author: wangliugeng
 * @Date : 2019-05-31
 * @Email: leo3552@163.com
 * @Desciption:
 */
public class DefaultLog implements ILog {
    @Override
    public void initLog(Context context) {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public void d(String msg) {
        Logger.d(msg);
    }

    @Override
    public void d(String tag, String msg) {
        Logger.d(tag, msg);
    }

    @Override
    public void e(String msg) {
        Logger.e(msg);
    }

    @Override
    public void e(String tag, String msg) {
        Logger.e(tag, msg);
    }

    @Override
    public void w(String msg) {
        Logger.w(msg);
    }

    @Override
    public void w(String tag, String msg) {
        Logger.w(tag, msg);
    }

    @Override
    public void json(String msg) {
        Logger.json(msg);
    }

    @Override
    public void json(String tag, String msg) {

    }
}
