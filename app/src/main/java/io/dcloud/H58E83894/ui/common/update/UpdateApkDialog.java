package io.dcloud.H58E83894.ui.common.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.ui.BasesDialog;
import io.dcloud.H58E83894.ui.common.BaseDialog;
import io.dcloud.H58E83894.utils.HtmlUtil;

public class UpdateApkDialog extends BasesDialog {

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
    ImageView cancelTxt;
    @BindView(R.id.dialog_simple_btn_confirm)
    ImageView confirmTxt;
    @BindView(R.id.frames)
    FrameLayout frameLayout;
    @BindView(R.id.update_tv)
    TextView updateTv;
    @BindView(R.id.update_tvss)
    TextView updateTvs;

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle arguments = getArguments();
        if (arguments == null) return;
//        contentTv.setText(HtmlUtil.fromHtml(arguments.getString(SIMPLE_DIALOG_CONTENT)));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        frameLayout.nt
//        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        view.measure(w, h);
//        view.getMeasuredWidth(); // 获取宽度
//        view.getMeasuredHeight(); // 获取高度
        updateTvs.setText("V2.30");

        updateTv.setText("1.添加了口语批改功能\n"+
                "2.添加了作文批改功能\n"+
                "3.修复bug\n");
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

    }

    @Override
    protected int getRootViewId() {
        return R.layout.update_apk_dialog_layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null)
            mCallBack = null;
    }
}
