package com.android.leo.base.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.leo.android.log.core.LogUtils;

import java.lang.reflect.Method;

/**
 *
 */
public class LayoutHelper {

    private static final String TAG = "NavigationBarHelper";

    public static void assistViewWithListener(View viewObserving, OnLayoutChangeListener navigationBarVisibleListener) {
        new LayoutHelper(viewObserving, navigationBarVisibleListener);
    }

    private boolean mLayoutComplete = false;
    private View mViewObserved;//被监听的视图
    private OnLayoutChangeListener navigationBarVisibleListener;
    private int usableHeightPrevious;//视图变化前的可用高度


    private LayoutHelper(View viewObserving, OnLayoutChangeListener navigationBarVisibleListener) {
        mViewObserved = viewObserving;
        mViewObserved.post(new Runnable() {
            @Override
            public void run() {
                mLayoutComplete = true;
                LogUtils.d(TAG, "content 布局完成");
            }
        });
        this.navigationBarVisibleListener = navigationBarVisibleListener;
        //给View添加全局的布局监听器
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (mLayoutComplete) {
                    resetLayoutByUsableHeight(computeUsableHeight());
                }
            }
        });
    }

    private void resetLayoutByUsableHeight(int usableHeightNow) {
        //比较布局变化前后的View的可用高度
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            LogUtils.d(TAG, "onGlobalLayout-->");
            if (navigationBarVisibleListener != null) {
                navigationBarVisibleListener.onLayoutChange(usableHeightNow < usableHeightPrevious);
            }
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算视图可视高度
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mViewObserved.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    public interface OnLayoutChangeListener {
        /**
         * @param visible true 底部虚拟按键显示  false otherwise
         */
        void onLayoutChange(boolean visible);
    }

    public static boolean hasNavigationBarFun(Activity activity) {
        Resources rs = activity.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        boolean hasNavBarFun = false;
        if (id > 0) {
            hasNavBarFun = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavBarFun = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavBarFun = true;
            }
        } catch (Exception e) {
            hasNavBarFun = false;
        }
        LogUtils.d(TAG, "hasNavigationBarFun->" + hasNavBarFun);
        return hasNavBarFun;
    }

    public static boolean checkNavigationBarShow(Activity activity) {

        boolean show;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Window window = activity.getWindow();
            Display display = window.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            View decorView = window.getDecorView();
            Configuration conf = activity.getResources().getConfiguration();
            if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
                View contentView = decorView.findViewById(android.R.id.content);
                show = (point.x != contentView.getWidth());
            } else {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                show = (rect.bottom != point.y);
            }
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                show = false;
            } else {
                show = true;
            }
        }

        return show;
    }


}
