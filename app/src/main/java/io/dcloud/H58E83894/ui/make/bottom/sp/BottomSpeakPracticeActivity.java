package io.dcloud.H58E83894.ui.make.bottom.sp;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;

public class BottomSpeakPracticeActivity extends BaseActivity {

    @BindView(R.id.speak_practice_tpo_container)
    RelativeLayout topContainer;
    @BindView(R.id.speak_practice_og_container)
    RelativeLayout bottomContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_speak_practice);
        topDownInAnim(topContainer, bottomContainer);
    }

    @OnClick({R.id.speak_practice_tpo_container, R.id.speak_practice_og_container})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.speak_practice_tpo_container:
                forword(SpeakTpoListActivity.class);
                break;
            case R.id.speak_practice_og_container:
                forword(SpeakGlodListActivity.class);
                break;
            default:
                break;
        }
    }
}
