package com.leowong.project.eyepetizer.ui.view.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.leowong.project.eyepetizer.utils.LogUtils;

public class CustomLayout extends LinearLayout {
    int yDif = 0;
    private SparseArray<BNPoint> pointArrayList = new SparseArray<>();//触控点列表

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
//        int id = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>>
//                MotionEvent.ACTION_POINTER_INDEX_SHIFT;//无符号右移获取触控点id
        int pointIndex = ev.getActionIndex();
        int pointId = ev.getPointerId(pointIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.INSTANCE.d("ACTION_DOWN");
                yDif = 0;
                pointArrayList.append(pointId, new BNPoint(ev.getX(pointIndex), ev.getY(pointIndex), pointId));
                break;
            case MotionEvent.ACTION_POINTER_DOWN://第一个之后的触控点按下
                LogUtils.INSTANCE.d("ACTION_POINTER_DOWN");
                pointArrayList.append(pointId, new BNPoint(ev.getX(pointIndex), ev.getY(pointIndex), pointId));
                break;
            case MotionEvent.ACTION_MOVE://主、辅点移动
                pointArrayList.get(pointId).setLocation(ev.getX(), ev.getY());
                for (int i = 0; i < pointArrayList.size(); i++) {
                    try {
                        int key = pointArrayList.keyAt(i);
                        BNPoint bnPoint = pointArrayList.get(key);
                        yDif += bnPoint.getDistanceY();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LogUtils.INSTANCE.d("y diff->" + yDif);
                break;
            case MotionEvent.ACTION_UP://最后一个点抬起
                LogUtils.INSTANCE.d("ACTION_UP");
                yDif = 0;
                pointArrayList.clear();
                break;
            case MotionEvent.ACTION_POINTER_UP://非最后一个点抬起
                LogUtils.INSTANCE.d("ACTION_POINTER_UP");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public class BNPoint {
        float xStart;//触控点x start坐标
        float yStart;//触控点y start坐标
        float x;//触控点x坐标
        float y;//触控点y坐标
        int id;//触控点id

        public BNPoint(float xStart, float yStart, int id) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.id = id;
        }

        public BNPoint(float xStart, float yStart) {
            this.xStart = xStart;
            this.yStart = yStart;
        }

        public void setLocation(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getDistanceX() {
            return x - xStart;
        }

        public float getDistanceY() {
            return y - yStart;
        }
    }
}
