package com.leo.android.log.core;

import android.content.Context;

public interface ILog {
    void initLog(Context context);

    void d(String msg);

    void d(String tag, String msg);

    void e(String msg);

    void e(String tag, String msg);

    void w(String msg);


    void w(String tag, String msg);


    void json(String msg);

    void json(String tag, String msg);
}
