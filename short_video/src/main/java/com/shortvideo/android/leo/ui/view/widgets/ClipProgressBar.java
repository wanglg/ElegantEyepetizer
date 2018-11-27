package com.shortvideo.android.leo.ui.view.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ClipDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.leowang.shortvideo.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class ClipProgressBar extends FrameLayout implements Animatable {
    private boolean running;
    private int progress = 0;
    private static final int MAX_PROGRESS = 10000;
    private ClipDrawable clip;
    private static final long FRAME_DURATION = 1000 / 60;
    private Disposable disposable;

    public ClipProgressBar(@NonNull Context context) {
        super(context);
        initView();
    }

    public ClipProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ClipProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        ImageView imageView = new ImageView(getContext());
        clip = (ClipDrawable) getResources().getDrawable(R.drawable.clip_progress);
        imageView.setImageDrawable(clip);
        imageView.setBackgroundColor(Color.parseColor("#80ffffff"));
        addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        start();
    }

    @Override
    public void start() {
        if (getVisibility() != VISIBLE || getWindowVisibility() != VISIBLE) {
            return;
        }
        if (disposable != null) {
            disposable.dispose();
        }
        reset();
        running = true;
        disposable = Flowable.interval(FRAME_DURATION, TimeUnit.MILLISECONDS).filter(new Predicate<Long>() {
            @Override
            public boolean test(Long aLong) {
                return getVisibility() == VISIBLE;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                if (progress == MAX_PROGRESS) {
                    progress = 0;
                }
                progress += 250;
                clip.setLevel(progress);
            }
        });

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stop();
        } else {
            start();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stop();
        } else {
            start();
        }
    }


    private void reset() {
        progress = 0;
    }


    @Override
    public void stop() {
        progress = 0;
        running = false;
        if (disposable != null) {
            disposable.dispose();
        }

    }

    @Override
    public boolean isRunning() {
        return running;
    }


}
