package io.dcloud.H58E83894.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.utils.MeasureUtil;
import io.dcloud.H58E83894.utils.Utils;

/**
 * 提供一个公共的对话框背景
 */
public abstract class BaseDialog extends BaseDialogView {

    @Override
    protected void initRootView(Bundle savedInstanceState) {
        ((FrameLayout) mRootView.findViewById(R.id.base_dialog_container))
                .addView(LayoutInflater.from(getActivity()).inflate(getContentViewLayId(), null));
    }


    @Override
    protected int getRootViewId() {
        return R.layout.base_dialog_bg;
    }

    /**
     * 获取ContentView
     */
    protected abstract int getContentViewLayId();

    protected String getEditTxt(EditText editText) {
        return Utils.getEditTextString(editText);
    }

    protected boolean getHttpCodeSucc(int code) {
        if (Utils.getHttpMsgSu(code)) {
            return true;
        }
        return false;
    }

    @Override
    protected int[] getWH() {
        int[] wh = {(int) (MeasureUtil.getScreenSize(getActivity()).x * 0.8), getDialog().getWindow().getAttributes().height};
        return wh;
    }

    @Override
    protected boolean isNoTitle() {
        return true;
    }
}
