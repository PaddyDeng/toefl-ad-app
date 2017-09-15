package io.dcloud.H58E83894.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.utils.MeasureUtil;

/**
 * Created by fire on 2017/7/18  16:18.
 */

public class TaskEndDialog extends BaseDialogView {

    @BindView(R.id.task_end_btn)
    TextView taskEndBtn;

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_task_end_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
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
