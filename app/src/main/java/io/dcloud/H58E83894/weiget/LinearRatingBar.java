package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.dcloud.H58E83894.R;


public class LinearRatingBar extends LinearLayout{

    private Drawable progressSrc;
    private Drawable background;
    private int ratingSpace;
    private int numStar;
    private int rating;
    private SparseArray<ImageView> ratingArray;

    public LinearRatingBar(Context context) {
        this(context, null);
    }

    public LinearRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultAttr(context);
        initCustomAttr(context, attrs);
        initView(context);
    }

    public void initDefaultAttr(Context context) {
        progressSrc = context.getResources().getDrawable(R.drawable.icon_course_light);
        background = context.getResources().getDrawable(R.drawable.icon_course_light);
        ratingSpace = 20;
        numStar = 5;
    }

    public void initCustomAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.linearratingbar);
        final int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            initCustomAttrDetail(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    public void initCustomAttrDetail(int attr, TypedArray typedArray) {
        if (attr == R.styleable.linearratingbar_progressSrc) {
            progressSrc = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.linearratingbar_defaultSrc) {
            background = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.linearratingbar_numStar) {
            numStar = typedArray.getInt(attr, numStar);
        } else if (attr == R.styleable.linearratingbar_ratingSpacing) {
            ratingSpace = typedArray.getDimensionPixelSize(attr, ratingSpace);
        } else if (attr == R.styleable.linearratingbar_rating) {
            rating = typedArray.getInt(attr, rating);
        }
    }

    public void initView(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        ratingArray = new SparseArray<>();
        for (int i = 0; i < numStar; i++) {
            ImageView iv = new ImageView(context);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = ratingSpace;
            iv.setLayoutParams(layoutParams);
            setBackgroundDra(iv,i);
            iv.setId(i);
            ratingArray.put(i, iv);
            addView(iv);
        }
    }

    private void setBackgroundDra(ImageView imageView,int i){
        if (i < rating) {
            imageView.setImageDrawable(progressSrc);
        } else {
            imageView.setImageDrawable(background);
        }
    }

    private void drawView() {
        for (int i = 0; i < numStar; i++) {
            ImageView imageView = ratingArray.get(i);
            setBackgroundDra(imageView,i);
        }
        invalidate();
    }

    public void setRating(int rating) {
        this.rating = rating;
        drawView();
    }
}
