package io.dcloud.H58E83894.ui.center.font;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FontControlContainer extends RelativeLayout {
    private ImageView bgImg;
    private View thumb;
    private int bgImgWidth;
    private int thumbWidth;
    private int containerLeft;
    private int containerRight;
    private FontSizeChangeListener mListener;

    public FontControlContainer(Context context) {
        this(context, null);
    }

    public FontControlContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontControlContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        thumbWidth = thumb.getWidth();
        bgImgWidth = bgImg.getWidth();
        containerLeft = getLeft();
        containerRight = containerLeft + getMeasuredWidth();
        if (fontSize != 0 && bgImgWidth != 0) {
            LayoutParams thumbLayoutParams = (LayoutParams) thumb.getLayoutParams();
            float space = bgImgWidth / 4.0f;//分成四等份
            thumbLayoutParams.leftMargin = (int) (space * fontSize);
            fontSize = 0;
            thumb.requestLayout();
        }
    }

    private int fontSize;

    public void setThumbLeftMargin(int index) {
        fontSize = index;
//        RelativeLayout.LayoutParams thumbLayoutParams = (RelativeLayout.LayoutParams) thumb.getLayoutParams();
//        float space = bgImgWidth / 4.0f;//分成四等份
//        thumbLayoutParams.leftMargin = (int) (space * index);
//        thumb.requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int rawLeft = (int) event.getRawX();
        LayoutParams layoutParams = (LayoutParams) thumb.getLayoutParams();
        if (rawLeft <= containerLeft + thumbWidth / 2) {
            layoutParams.leftMargin = 0;
        } else if (rawLeft >= containerRight - thumbWidth) {
            layoutParams.leftMargin = containerRight - containerLeft - thumbWidth;
        } else {
            layoutParams.leftMargin = rawLeft - containerLeft - thumbWidth / 2;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) thumb.getLayoutParams();
//                if (rawLeft <= containerLeft + thumbWidth / 2) {
//                    layoutParams.leftMargin = 0;
//                } else if (rawLeft >= containerRight - thumbWidth) {
//                    layoutParams.leftMargin = containerRight - containerLeft - thumbWidth;
//                } else {
//                    layoutParams.leftMargin = rawLeft - containerLeft - thumbWidth / 2;
//                }
                break;
            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
                calc();
                break;
        }
        thumb.requestLayout();
        return true;
    }

    private void calc() {
        LayoutParams thumbLayoutParams = (LayoutParams) thumb.getLayoutParams();
        float space = bgImgWidth / 4.0f;//分成四等份
        int thumbDistance = thumbLayoutParams.leftMargin;// + thumbWidth / 2.0f;
        int index = (int) (thumbDistance / space);
        int extra = thumbDistance % ((int) space);
        if (extra > space / 2.0f) {
            index++;
        }
        thumbLayoutParams.leftMargin = (int) (space * index);
        if (mListener != null)
            mListener.fontSize(index);
    }

    public void setFontSizeChangeListener(FontSizeChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bgImg = (ImageView) getChildAt(0);
        thumb = getChildAt(1);
    }

    public interface FontSizeChangeListener {
        void fontSize(int index);
    }

}
