package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Fire on 2017/7/28.
 */
public class VerticalScrollImageView extends ImageView implements ViewTreeObserver.OnScrollChangedListener {

    private int[] viewLocation = new int[2];
    private boolean enableScrollParallax = true;

    public VerticalScrollImageView(Context context) {
        this(context, null);
    }

    public VerticalScrollImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalScrollImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (!enableScrollParallax || getDrawable() == null) {
            super.onDraw(canvas);
            return;
        }

        getLocationInWindow(viewLocation);
        transform(canvas, viewLocation[0], viewLocation[1]);

        super.onDraw(canvas);
    }

    private void transform(Canvas canvas, int x, int y) {
        if (getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            return;
        }

        // image's width and height
        int iWidth = getDrawable().getIntrinsicWidth();
        int iHeight = getDrawable().getIntrinsicHeight();
        if (iWidth <= 0 || iHeight <= 0) {
            return;
        }

        // view's width and height
        int vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int vHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        // device's height
        int dHeight = getResources().getDisplayMetrics().heightPixels;

        if (iWidth * vHeight < iHeight * vWidth) {
            // avoid over scroll
            if (y < -vHeight) {
                y = -vHeight;
            } else if (y > dHeight) {
                y = dHeight;
            }

            float imgScale = (float) vWidth / (float) iWidth;
            float max_dy = Math.abs((iHeight * imgScale - vHeight) * 0.5f);
            float translateY = -(2 * max_dy * y + max_dy * (vHeight - dHeight)) / (vHeight + dHeight);
            canvas.translate(0, translateY);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnScrollChangedListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        getViewTreeObserver().removeOnScrollChangedListener(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onScrollChanged() {
        if (enableScrollParallax) {
            postInvalidate();
        }
    }
}
