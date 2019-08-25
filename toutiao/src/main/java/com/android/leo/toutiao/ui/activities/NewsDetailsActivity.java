package com.android.leo.toutiao.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.agile.android.leo.mvp.IPresenter;
import com.android.leo.base.ui.activities.BaseActivity;
import com.android.leo.toutiao.R;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.smtt.sdk.WebView;

import org.jetbrains.annotations.Nullable;

/**
 * @Author: wangliugeng
 * @Date : 2019-08-23
 * @Email: leo3552@163.com
 * @Desciption:
 */
public class NewsDetailsActivity extends BaseActivity<IPresenter> {
    public static final String URL = "url";
    // 网页链接
    private String mUrl;
    private WebView webView;
    private ImageView mIvBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_details_view;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).init();
        webView = findViewById(R.id.wv_content);
        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra(URL);
    }

    @Override
    public void requestData() {

    }
}
