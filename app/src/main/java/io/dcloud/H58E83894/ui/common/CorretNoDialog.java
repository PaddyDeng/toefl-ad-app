package io.dcloud.H58E83894.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.make.SpeakQuestionData;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.BasesDialog;
import io.dcloud.H58E83894.ui.make.lexicalResource.PricePayCorretActivity;

public class CorretNoDialog extends BasesDialog {

    private static ICallBack<String> mCallBack;
    private static final String SIMPLE_DIALOG_CONTENT = "dialog_content";
    protected RxPermissions mRxPermissions;
    private List<SpeakQuestionData> mSpeakDatas;


    public static CorretNoDialog getInstance(String content) {
        CorretNoDialog simpleDialog = new CorretNoDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SIMPLE_DIALOG_CONTENT, content);
        simpleDialog.setArguments(bundle);
        return simpleDialog;
    }

    @BindView(R.id.dialog_simple_btn_cancel)
    ImageView cancelTxt;
    @BindView(R.id.update_tvss)
    TextView updateTv;
    private String price;


    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle arguments = getArguments();
        if (arguments == null) return;
       price = arguments.getString(SIMPLE_DIALOG_CONTENT);
//        contentTv.setText(HtmlUtil.fromHtml(arguments.getString(SIMPLE_DIALOG_CONTENT)));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        updateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    PricePayCorretActivity.startPre(getContext(), price);
                    mCallBack = null;
                dismiss();
            }
        });

    }

    @Override
    protected int getRootViewId() {
        return R.layout.corret_no;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null)
            mCallBack = null;
    }


}
