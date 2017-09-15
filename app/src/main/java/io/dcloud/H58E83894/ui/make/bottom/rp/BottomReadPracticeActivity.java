package io.dcloud.H58E83894.ui.make.bottom.rp;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;

public class BottomReadPracticeActivity extends BaseActivity {

    @BindView(R.id.read_practice_tpo_container)
    RelativeLayout mTpoContainer;
    @BindView(R.id.read_practice_og_container)
    RelativeLayout mOgContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_read_practice);
        topDownInAnim(mTpoContainer, mOgContainer);
    }

    @OnClick({R.id.read_practice_tpo_container,R.id.read_practice_og_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_practice_tpo_container:
                forword(ReadTpoListActivity.class);
                break;
            case R.id.read_practice_og_container:
                forword(ReadOgListActivity.class);
                break;
            default:
                break;
        }
    }

}
