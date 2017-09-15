package io.dcloud.H58E83894.weiget.commentview.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.ToeflApplication;


public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.color_orange;
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = ToeflApplication.getInstance().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
