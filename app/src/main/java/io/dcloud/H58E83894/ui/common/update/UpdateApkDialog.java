package io.dcloud.H58E83894.ui.common.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.ui.common.BaseDialog;
import io.dcloud.H58E83894.utils.HtmlUtil;

public class UpdateApkDialog extends BaseDialog {

    private static ICallBack<String> mCallBack;
    private static final String SIMPLE_DIALOG_CONTENT = "dialog_content";

    public static UpdateApkDialog getInstance(String content, ICallBack<String> callBack) {
        UpdateApkDialog simpleDialog = new UpdateApkDialog();
        simpleDialog.mCallBack = callBack;
        Bundle bundle = new Bundle();
        bundle.putString(SIMPLE_DIALOG_CONTENT, content);
        simpleDialog.setArguments(bundle);
        return simpleDialog;
    }

    @BindView(R.id.dialog_simple_btn_cancel)
    TextView cancelTxt;
    @BindView(R.id.dialog_simple_btn_confirm)
    TextView confirmTxt;
    @BindView(R.id.simple_dialog_content)
    TextView contentTv;

    @Override
    protected int getContentViewLayId() {
        return R.layout.update_apk_dialog_layout;
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle arguments = getArguments();
        if (arguments == null) return;
        contentTv.setText(HtmlUtil.fromHtml(arguments.getString(SIMPLE_DIALOG_CONTENT)));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onFail();
                    mCallBack = null;
                }
                dismiss();
            }
        });
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onSuccess("");
                    mCallBack = null;
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null)
            mCallBack = null;
    }
}
