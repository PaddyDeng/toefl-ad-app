package io.dcloud.H58E83894.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import io.dcloud.H58E83894.R;

public class ButtonSpan extends ClickableSpan {
  
    View.OnClickListener onClickListener;
    private Context context;
    private int colorId;  
  
    public ButtonSpan(Context context, View.OnClickListener onClickListener) {  
        this(context, onClickListener, R.color.color_blue);
    }  
  
    public ButtonSpan(Context context, View.OnClickListener onClickListener, int colorId){  
        this.onClickListener = onClickListener;  
        this.context = context;  
        this.colorId = colorId;  
    }  
  
    @Override  
    public void updateDrawState(TextPaint ds) {
        ds.setColor(context.getResources().getColor(colorId));  
        ds.setUnderlineText(false);  
    }  
  
    @Override  
    public void onClick(View widget) {  
        if (onClickListener != null) {  
            onClickListener.onClick(widget);  
        }  
    }  
}  