package io.dcloud.H58E83894.ui.common.grammar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.ui.common.BaseDialogView;
import io.dcloud.H58E83894.utils.MeasureUtil;

/**
 * Created by fire on 2017/7/18  14:19.
 */

public class AnalyzeDialog extends BaseDialogView {
    @BindView(R.id.analyze_tv)
    TextView contentTv;
    @BindView(R.id.close_analyze_dialog_iv)
    ImageView closeIv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setWindowAnimations(R.style.analyze_anim_style);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static AnalyzeDialog getInstance(String content) {
        AnalyzeDialog analyzeDialog = new AnalyzeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("CONTENT", content);
        analyzeDialog.setArguments(bundle);
        return analyzeDialog;
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
    protected void getArgs() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        String content = arguments.getString("CONTENT");
        if (!TextUtils.isEmpty(content)) {
            contentTv.setText(content);
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_analyzed_layout;
    }


    @Override
    protected int[] getWH() {
        int[] wh = {MeasureUtil.getScreenSize(getActivity()).x, MeasureUtil.getScreenSize(getActivity()).y};
        return wh;
    }

    @Override
    protected boolean isNoTitle() {
        return true;
    }

}
