package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import io.dcloud.H58E83894.R;

/**
 * Created by fire on 2017/7/12.应用于口语练习模块中
 */

public class RingView extends View {

    private int mBorderWidth;
    private int mBorderColor;
    private float mBorderRadius;
    private Paint mBorderPaint = new Paint();
    private Rect mBorderRect = new Rect();

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * context通过调用obtainStyledAttributes方法来获取一个TypeArray，然后由该TypeArray来对属性进行设置
         obtainStyledAttributes方法有三个，我们最常用的是有一个参数的obtainStyledAttributes(int[] attrs)，其参数直接styleable中获得
         * */
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RingView, defStyleAttr, 0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.RingView_ring_width, 0);
        mBorderColor = a.getColor(R.styleable.RingView_ring_color, Color.BLACK);
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    private void init() {
        mBorderPaint.setStyle(Paint.Style.STROKE);//绘制空心圆
        mBorderPaint.setAntiAlias(true);//消除锯齿
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
    }
}
