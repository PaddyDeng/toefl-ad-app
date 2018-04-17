package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;


public class DownloadScheduleView extends android.support.v7.widget.AppCompatImageView {

    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private Paint mBitmapPaint;
    private float progress = 0f;
    private final float maxProgress = 100f;
    int roundColor;
    int roundProgressColor;
    private float mStrokeWidth = 40f;
    private boolean pause = true;
    public Bitmap downloading;
    public Bitmap pauseLoading;

    public DownloadScheduleView(Context context) {
        this(context, null);
    }

    public DownloadScheduleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        roundColor = getResources().getColor(R.color.color_sec_gray);
        roundProgressColor = getResources().getColor(R.color.color_blue);
        downloading = BitmapFactory.decodeResource(getResources(), R.drawable.icon_downloading);
        pauseLoading = BitmapFactory.decodeResource(getResources(), R.drawable.icon_download_pause);
        // 初始化点击事件
        initClickListener();
    }

    private void initClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                pause = !pause;
                postInvalidate();//重画
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onClick(null, 0);
            }
        });
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 控制状态
     */
    public void setPause(boolean pause) {
        this.pause = pause;
        postInvalidate();
    }

    public boolean isPause() {
        return pause;
    }

    public void onDestory() {
        if (pauseLoading != null)
            pauseLoading.recycle();
        if (downloading != null)
            downloading.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private void initValue() {
        // 画笔的粗细为总宽度的 1 / 15
        mStrokeWidth = mWidth / 15f;
    }

    private void setupPaint() {
        // 创建圆环画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(roundColor);
        mPaint.setStyle(Paint.Style.STROKE); // 边框风格
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    private void setupTextPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 第一步：绘制一个圆环
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(roundColor);

        float cx = mWidth / 2.0f;
        float cy = mHeight / 2.0f;
        float radius = mWidth / 2.0f - mStrokeWidth / 2.0f;
        canvas.drawCircle(cx, cy, radius, mPaint);

        // 第二步：绘制bitmap
        if (pause) {
            canvas.drawBitmap(pauseLoading, mWidth / 2.0f - pauseLoading.getWidth() / 2.0f, mHeight / 2.0f - pauseLoading.getHeight() / 2.0f, mBitmapPaint);
        } else {
            canvas.drawBitmap(downloading, mWidth / 2.0f - downloading.getWidth() / 2.0f, mHeight / 2.0f - downloading.getHeight() / 2.0f, mBitmapPaint);
        }

        // 第三步：绘制动态进度圆环
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setStrokeCap(Paint.Cap.ROUND); //  设置笔触为圆形

        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(roundProgressColor);
        RectF oval = new RectF(0 + mStrokeWidth / 2, 0 + mStrokeWidth / 2,
                mWidth - mStrokeWidth / 2, mHeight - mStrokeWidth / 2);

        canvas.drawArc(oval, -90, progress / maxProgress * 360, false, mPaint);
    }

    /**
     * 设置当前显示的进度条
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.progress = progress;

        // 使用 postInvalidate 比 postInvalidat() 好，线程安全
        postInvalidate();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 初始化各种值
        initValue();

        // 设置圆环画笔
        setupPaint();

        setupTextPaint();
    }

}
