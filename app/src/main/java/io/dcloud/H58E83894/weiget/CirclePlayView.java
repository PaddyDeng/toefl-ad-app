package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class CirclePlayView extends View {
    private static final String TAG = "CircleTimerView";

    // Status
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_RADIAN = "status_radian";

    // Default dimension in dp/pt
    private static final float DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 3;
    private static final float DEFAULT_NUMBER_SIZE = 1;
    private static final float DEFAULT_LINE_WIDTH = 0.5f;
    private static final float DEFAULT_CIRCLE_BUTTON_RADIUS = 8;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 1;

    // Default color
    private static final int DEFAULT_CIRCLE_BUTTON_COLOR = 0xFFEDEDED;//圆
    private static final int DEFAULT_LINE_COLOR = 0xFFFF6600;//进度
    private static final int DEFAULT_NUMBER_COLOR = 0xFFC8C8C8;//背景线

    // Paint
    private Paint mLinePaint;
    private Paint mCircleButtonPaint;
    private Paint mNumberPaint;

    // Dimension
    private float mGapBetweenCircleAndLine;
    private float mNumberSize;
    private float mLineWidth;
    private float mCircleButtonRadius;
    private float mCircleStrokeWidth;

    // Color
    private int mCircleButtonColor;
    private int mLineColor;
    private int mNumberColor;

    // Parameters
    private float mCx;
    private float mCy;
    private float mRadius;
    private float mCurrentRadian;
    private float mCurrentRadian1;
    private float mPreRadian;
    private boolean mInCircleButton;
    private int mCurrentTime; // seconds

    private int max = 3600;

    public void setProgress(int progress) {
        if (progress >= max) return;
        mCurrentTime = progress;
        mCurrentRadian = (float) ((2 * Math.PI * progress) / max);
        postInvalidate();
    }

    public void setMax(int max) {
        this.max = max;
    }

    private OnTimeChangedListener mListener;

    public CirclePlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CirclePlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePlayView(Context context) {
        this(context, null);
    }

    private void initialize() {
        Log.d(TAG, "initialize");
        // Set default dimension or read xml attributes
        mGapBetweenCircleAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE,
                getContext().getResources().getDisplayMetrics());
        mNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_NUMBER_SIZE, getContext().getResources()
                .getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_WIDTH, getContext().getResources()
                .getDisplayMetrics());
        mCircleButtonRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_RADIUS, getContext()
                .getResources().getDisplayMetrics());
        mCircleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_STROKE_WIDTH, getContext()
                .getResources().getDisplayMetrics());

        // Set default color or read xml attributes
        mCircleButtonColor = DEFAULT_CIRCLE_BUTTON_COLOR;
        mLineColor = DEFAULT_LINE_COLOR;
        mNumberColor = DEFAULT_NUMBER_COLOR;

        // Init all paints
        mCircleButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // CircleButtonPaint
        mCircleButtonPaint.setColor(mCircleButtonColor);
        mCircleButtonPaint.setAntiAlias(true);
        mCircleButtonPaint.setStyle(Paint.Style.FILL);

        // LinePaint
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mCircleButtonRadius / 2);
        mLinePaint.setStyle(Paint.Style.STROKE);


        // NumberPaint
        mNumberPaint.setColor(mNumberColor);
        mNumberPaint.setTextSize(mNumberSize);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);
        mNumberPaint.setStyle(Paint.Style.STROKE);
        mNumberPaint.setStrokeWidth(mCircleButtonRadius / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();

        canvas.drawCircle(mCx, mCy, mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine, mNumberPaint);
        canvas.save();
        canvas.rotate(-90, mCx, mCy);
        RectF rect = new RectF(mCx - (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
        ), mCy - (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
        ), mCx + (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
        ), mCy + (mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine
        ));

        if (mCurrentRadian1 > mCurrentRadian) {
            canvas.drawArc(rect, (float) Math.toDegrees(mCurrentRadian1), (float) Math.toDegrees(2 * (float) Math.PI) - (float) Math.toDegrees(mCurrentRadian1) + (float) Math.toDegrees(mCurrentRadian), false, mLinePaint);
        } else {
            canvas.drawArc(rect, (float) Math.toDegrees(mCurrentRadian1), (float) Math.toDegrees(mCurrentRadian) - (float) Math.toDegrees(mCurrentRadian1), false, mLinePaint);
        }
        canvas.restore();
        canvas.save();

        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine
                , 0.01f, mLinePaint);
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian1), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, 0.01f, mLinePaint);
        canvas.restore();
        // TimerNumber
        canvas.save();

        canvas.rotate((float) Math.toDegrees(mCurrentRadian1), mCx, mCy);
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCircleButtonRadius, mCircleButtonPaint);
        canvas.restore();
        // TimerNumber
        canvas.save();

        canvas.restore();
        // Timer Text
//        canvas.save();
//        canvas.restore();
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // If the point in the circle button
                if (mListener != null) {
                    mListener.down();
                }
                if (mInCircleButton(event.getX(), event.getY()) && isEnabled()) {
                    mInCircleButton = true;
                    mPreRadian = getRadian(event.getX(), event.getY());
                    Log.d(TAG, "In circle button");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInCircleButton && isEnabled()) {
                    float temp = getRadian(event.getX(), event.getY());
                    if (mPreRadian > Math.toRadians(270) && temp < Math.toRadians(90)) {
                        mPreRadian -= 2 * Math.PI;
                    } else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270)) {
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
                    }
                    mCurrentRadian += (temp - mPreRadian);
                    mPreRadian = temp;
                    if (mCurrentRadian > 2 * Math.PI) {
                        mCurrentRadian -= (float) (2 * Math.PI);
                    }
                    if (mCurrentRadian < 0) {
                        mCurrentRadian += (float) (2 * Math.PI);
                    }
                    cacl();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    mListener.up();
                }
                if (mInCircleButton && isEnabled()) {
                    mInCircleButton = false;
                }
                break;
        }
        return true;
    }

    private void cacl() {
        mCurrentTime = (int) (max / (2 * Math.PI) * mCurrentRadian);
        if (null != mListener) {
            mListener.progress(mCurrentTime);
        }
    }

    // Whether the down event inside circle button
    private boolean mInCircleButton(float x, float y) {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine;
        float x2 = (float) (mCx + r * Math.sin(mCurrentRadian));
        float y2 = (float) (mCy - r * Math.cos(mCurrentRadian));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < mCircleButtonRadius) {
            return true;
        }
        return false;
    }

    // Use tri to cal radian
    private float getRadian(float x, float y) {
        float alpha = (float) Math.atan((x - mCx) / (mCy - y));
        // Quadrant
        if (x > mCx && y > mCy) {
            // 2
            alpha += Math.PI;
        } else if (x < mCx && y > mCy) {
            // 3
            alpha += Math.PI;
        } else if (x < mCx && y < mCy) {
            // 4
            alpha = (float) (2 * Math.PI + alpha);
        }
        return alpha;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Ensure width = height
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.mCx = width / 2;
        this.mCy = height / 2;
        // Radius
        if (mGapBetweenCircleAndLine + mCircleStrokeWidth >= mCircleButtonRadius) {
            this.mRadius = width / 2 - mCircleStrokeWidth / 2;
        } else {
            this.mRadius = width / 2 - (mCircleButtonRadius - mGapBetweenCircleAndLine -
                    mCircleStrokeWidth / 2);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_RADIAN, mCurrentRadian);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            mCurrentRadian = bundle.getFloat(STATUS_RADIAN);
            cacl();
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setOnTimeChangedListener(OnTimeChangedListener listener) {
        if (null != listener) {
            this.mListener = listener;
        }
    }

    public interface OnTimeChangedListener {
        void progress(int progress);

        void down();

        void up();
    }
}
