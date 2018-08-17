package com.leowong.project.eyepetizer.ui.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leowong.project.eyepetizer.R;

import java.util.ArrayList;

public class VideoRecommendAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public VideoRecommendAdapter(int layoutResId, @Nullable ArrayList<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.content, item);
    }
}
