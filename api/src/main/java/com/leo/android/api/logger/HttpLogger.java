package com.leo.android.api.logger;


import com.leo.android.log.core.LogUtils;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private StringBuilder mMessage = new StringBuilder();
    private String json;

    @Override
    public void log(String message) {
        // 请求或者响应开始
        if (message.startsWith("--> START HTTP")) {
            mMessage.setLength(0);
            mMessage.append("\n");
        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            json = message;
            return;
        }
        mMessage.append(message.concat("\n"));
        // 请求或者响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            LogUtils.e("HTTP_LOG", "\n--------------------------------------------\n");
            LogUtils.d("HTTP_LOG", mMessage.toString());
            LogUtils.e("HTTP_LOG", "\n--------------------------------------------\n\n\n\n");
            LogUtils.json("HTTP_LOG", "\n" + json);
            LogUtils.e("HTTP_LOG", "--------------------------------------------\n\n\n\n");
        }
    }
}
