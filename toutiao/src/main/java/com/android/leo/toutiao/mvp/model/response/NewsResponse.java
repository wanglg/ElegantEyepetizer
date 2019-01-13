package com.android.leo.toutiao.mvp.model.response;

import android.support.annotation.NonNull;

import com.android.leo.toutiao.mvp.model.entity.NewsData;
import com.android.leo.toutiao.mvp.model.entity.TipEntity;
import com.google.gson.Gson;

import java.util.ArrayList;


public class NewsResponse {

    public int login_status;
    public int total_number;
    public boolean has_more;
    public String post_content_hint;
    public int show_et_status;
    public int feed_flag;
    public int action_to_last_stick;
    public String message;
    public boolean has_more_to_refresh;
    public TipEntity tips;

    public ArrayList<NewsData> data;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
