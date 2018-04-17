package io.dcloud.H58E83894.weiget.font;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class ControlTextView extends android.support.v7.widget.AppCompatTextView {

    public ControlTextView(Context context) {
        super(context);
    }

    public ControlTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFontSize(int index) {
        switch (index) {
            case 0:
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                break;
            case 1:
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                break;
            case 2:
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                break;
            case 3:
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
            case 4:
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                break;
            default:
                break;
        }
    }

}
