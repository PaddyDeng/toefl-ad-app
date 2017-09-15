package io.dcloud.H58E83894.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.ui.user.UserProxyActivity;

/**
 * Created by fire on 2017/8/15  15:18.
 */

public class SimpleLoginTipDialog extends BaseDialog {

    @BindView(R.id.login_dialog_determine)
    TextView determine;
    @BindView(R.id.login_dialog_cancel)
    TextView cancel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), UserProxyActivity.class));
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getContentViewLayId() {
        return R.layout.dialog_login_tip_layout;
    }

}
