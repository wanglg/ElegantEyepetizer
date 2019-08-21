package com.leo.android.log.logan;

import android.content.Context;
import android.os.Environment;

import com.dianping.logan.Logan;
import com.leo.android.log.core.ILog;

import java.io.File;

/**
 * @Author: wangliugeng
 * @Date : 2019-05-30
 * @Email: leo3552@163.com
 * @Desciption:
 */
public class LogAnAgent implements ILog {
    private static final String FILE_NAME = "logan_v1";
    private ILog iLog;

    public LogAnAgent(ILog iLog) {
        this.iLog = iLog;
    }

    @Override
    public void initLog(Context context) {
        this.iLog.initLog(context);
    }

    @Override
    public void d(String msg) {
        iLog.d(msg);
        Logan.w(msg, 1);
    }

    @Override
    public void d(String tag, String msg) {
        iLog.d(tag, msg);
    }

    @Override
    public void e(String msg) {
        iLog.e(msg);
    }

    @Override
    public void e(String tag, String msg) {
        iLog.e(tag, msg);
    }

    @Override
    public void w(String msg) {
        iLog.w(msg);
    }

    @Override
    public void w(String tag, String msg) {
        iLog.w(tag, msg);
    }

    @Override
    public void json(String msg) {

    }

    @Override
    public void json(String tag, String msg) {

    }

    public String getDiskFileDir(Context context) {
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
}
