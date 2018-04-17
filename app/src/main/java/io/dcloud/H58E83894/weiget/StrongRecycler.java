package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class StrongRecycler extends RecyclerView {
    public StrongRecycler(Context context) {
        this(context, null);
    }

    public StrongRecycler(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrongRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int mLastXIntercept;
    private int mLastYIntercept;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) ev.getX() - mLastXIntercept;
                int deltaY = (int) ev.getY() - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {//横向滑动
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {//纵向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastXIntercept = x;
        mLastYIntercept = y;

        return super.dispatchTouchEvent(ev);
    }
}
