package com.shortvideo.android.leo.ui.view.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

/**
 *
 */
public class RecyclerPagerView extends RecyclerView implements OnPageStateChangedListener, ViewTreeObserver.OnGlobalLayoutListener {

    PagerSnapHelper pagerSnapHelper;
    private int currentPosition;

    public RecyclerPagerView(Context context) {
        super(context);
        attachSnap();
    }

    public RecyclerPagerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attachSnap();
    }

    public RecyclerPagerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        attachSnap();
    }

    public void attachSnap() {
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(this);
        pagerSnapHelper.addOnPageChangedListener(this);
    }


    public void addOnPageChangedListener(OnPageStateChangedListener listener) {
        if (pagerSnapHelper != null) {
            pagerSnapHelper.addOnPageChangedListener(listener);
        }
    }

    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }

    public void removeOnPageChangedListener(OnPageStateChangedListener listener) {
        if (pagerSnapHelper != null) {
            pagerSnapHelper.removeOnPageChangedListener(listener);
        }
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        if (pagerSnapHelper != null) {
            pagerSnapHelper.scrollToPosition(position);
        }

    }

    public void clearOnPageChangedListeners() {
        if (pagerSnapHelper != null) {
            pagerSnapHelper.clearOnPageChangedListeners();
        }
    }

    @Override
    public void onPageChanged(int oldPosition, int newPosition) {
        currentPosition = newPosition;
    }

    @Override
    public void onFlingToOtherPosition() {
        Log.d("@", "onFlingToOtherPosition");
    }

    @Override
    public void onGlobalLayout() {

    }

    @Override
    public void onPageDetachedFromWindow(int position) {

    }
}
