package com.leo.android.log.core;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.dianping.logan.OnLoganProtocolStatus;
import com.leo.android.log.DefaultLog;
import com.leo.android.log.logan.LogAnAgent;

import java.io.File;

/**
 * @Author: wangliugeng
 * @Date : 2019-05-30
 * @Email: leo3552@163.com
 * @Desciption:
 */
public class LogUtils {
    private static final String FILE_NAME = "logan_v1";
    private static ILog logAnAgent;

    public static void init(Context context, boolean isDebug, ILog log) {
        logAnAgent = new LogAnAgent(log);
        logAnAgent.initLog(context);
        LoganConfig config = new LoganConfig.Builder()
                .setCachePath(context.getFilesDir().getAbsolutePath())
                .setPath(getDiskFileDir(context)
                        + File.separator + FILE_NAME)
                .setEncryptKey16("19891213".getBytes())
                .setEncryptIV16("19891213".getBytes())
                .build();
        Logan.init(config);
        Logan.setDebug(isDebug);
        Logan.setOnLoganProtocolStatus(new OnLoganProtocolStatus() {
            @Override
            public void loganProtocolStatus(String cmd, int code) {
                Log.d("LogAn", "clogan > cmd : " + cmd + " | " + "code : " + code);
            }
        });
    }

    public static void init(Context context, boolean isDebug) {
        init(context, isDebug, new DefaultLog());
    }

    public static String getDiskFileDir(Context context) {
        String fileDir = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                File file = context.getExternalFilesDir(null);
                if (file != null) {
                    fileDir = file.getAbsolutePath();
                }
            } else {
                fileDir = context.getFilesDir().getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileDir == null) {
                fileDir = context.getFilesDir().getPath();
            }
        }
        return fileDir;
    }


    public static void d(String msg) {
        logAnAgent.d(msg);
    }

    public static void d(String tag, String msg) {
        logAnAgent.d(tag, msg);
    }

    public static void e(String msg) {
        logAnAgent.e(msg);
    }

    public static void e(String tag, String msg) {
        logAnAgent.e(tag, msg);
    }

    public static void w(String msg) {
        logAnAgent.w(msg);
    }

    public static void w(String tag, String msg) {
        logAnAgent.w(tag, msg);
    }

    public static void json(String msg) {
        logAnAgent.json(msg);
    }

    public static void json(String tag, String msg) {
        logAnAgent.json(tag, msg);
    }
}
