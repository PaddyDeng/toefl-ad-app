package io.dcloud.H58E83894.ui.center.font;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class ControlTextView extends TextView {

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

    public void setOptionWidthH(int fontSize) {
        switch (fontSize) {
            case 0:
                setWidth(16);
                setHeight(16);
                break;
            case 1:
                setWidth(18);
                setHeight(18);
                break;
            case 2:
                setWidth(20);
                setHeight(20);
                break;
            case 3:
                setWidth(22);
                setHeight(22);
                break;
            case 4:
                setWidth(25);
                setHeight(25);
                break;
            default:
                break;
        }
    }

}
