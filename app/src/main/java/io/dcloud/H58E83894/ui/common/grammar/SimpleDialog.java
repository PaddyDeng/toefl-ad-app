package io.dcloud.H58E83894.ui.common.grammar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.ui.common.BaseDialog;

/**
 * Created by fire on 2017/7/18  10:03.
 */
public class SimpleDialog extends BaseDialog {

    @BindView(R.id.simple_des)
    TextView simpleDesTv;
    @BindView(R.id.close_dialog_iv)
    ImageView closeIv;

    public static SimpleDialog getInstance(String content) {
        SimpleDialog simpleDialog = new SimpleDialog();
        Bundle bundle = new Bundle();
        bundle.putString("CONTENT", content);
        simpleDialog.setArguments(bundle);
        return simpleDialog;
    }

    @Override
    protected void getArgs() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        String content = arguments.getString("CONTENT");
        if (!TextUtils.isEmpty(content)) {
            simpleDesTv.setText(content);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getContentViewLayId() {
        return R.layout.dialog_simple_layout;
    }
}
