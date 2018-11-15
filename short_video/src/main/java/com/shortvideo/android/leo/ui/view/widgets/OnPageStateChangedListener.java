package com.shortvideo.android.leo.ui.view.widgets;

/**
 *
 */
public interface OnPageStateChangedListener {
    /**
     * @param oldPosition old position
     * @param newPosition new position
     */
    void onPageChanged(int oldPosition, int newPosition);

    /**
     * 快速滑动后在onFling中切换到其他position,触发回调，仿抖音，在这里移除播放器
     */
    void onFlingToOtherPosition();

    /*位于该position 的item从window detach*/
    void onPageDetachedFromWindow(int position);


}
