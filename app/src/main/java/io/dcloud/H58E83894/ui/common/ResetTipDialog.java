package io.dcloud.H58E83894.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.ICallBack;

public class ResetTipDialog extends BaseDialog {

    private static ICallBack<String> mCallBack;

    public static ResetTipDialog getInstance(ICallBack<String> callBack) {
        ResetTipDialog simpleDialog = new ResetTipDialog();
        simpleDialog.mCallBack = callBack;
        return simpleDialog;
    }

    @BindView(R.id.reset_dialog_simple_btn_cancel)
    TextView cancelTxt;
    @BindView(R.id.reset_dialog_simple_btn_confirm)
    TextView confirmTxt;

    @Override
    protected int getContentViewLayId() {
        return R.layout.reset_tip_dialog_layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null) {
            mCallBack = null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onSuccess("");
                    mCallBack = null;
                }
                dismiss();
            }
        });
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onFail();
                    mCallBack = null;
                }
                dismiss();
            }
        });
    }

}
