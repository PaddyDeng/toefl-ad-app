package io.dcloud.H58E83894.ui.prelesson;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.prelesson.FreeCursorData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.utils.C;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 由ToeflDetailActivity进来的试听按钮点的
 * */

public class TrialCourseTypeActivity extends BaseActivity {

    private String TRIAL_SC;
    private String TRIAL_RC;
    private String TRIAL_CR;
    private String TRIAL_MATH;
    @BindView(R.id.toefl_one_title)
    TextView oneTitleTv;
    @BindView(R.id.toefl_two_title)
    TextView twoTitleTv;
    @BindView(R.id.toefl_thr_title)
    TextView thrTitleTv;
    @BindView(R.id.toefl_four_title)
    TextView fourTitleTv;

    private void refreshUi(List<FreeCursorData> datas) {
        if (datas == null || datas.isEmpty()) return;
        for (int i = 0, size = datas.size(); i < size; i++) {
            FreeCursorData data = datas.get(i);
            if (i == 0) {
                setItemTxt(oneTitleTv, data);
                TRIAL_SC = data.getUrl();
            } else if (i == 1) {
                setItemTxt(twoTitleTv, data);
                TRIAL_RC = data.getUrl();
            } else if (i == 2) {
                setItemTxt(thrTitleTv, data);
                TRIAL_CR = data.getUrl();
            } else if (i == 3) {
                setItemTxt(fourTitleTv, data);
                TRIAL_MATH = data.getUrl();
            }
        }
    }

    private void setItemTxt(TextView titleTv, FreeCursorData data) {
        titleTv.setText(data.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_course_type);
    }

    private void startDeal(String title, String url) {
        if (TextUtils.isEmpty(url)) return;
        DealActivity.startDealActivity(mContext, title, url);
    }

    @Override
    protected void initData() {
        addToCompositeDis(HttpUtil
                .getFreeCursor()
                .subscribe(new Consumer<List<FreeCursorData>>() {
                    @Override
                    public void accept(@NonNull List<FreeCursorData> datas) throws Exception {
                        refreshUi(datas);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorTip(throwable);
                    }
                }));
    }

    @OnClick({R.id.trial_sc_container, R.id.trial_rc_container, R.id.trial_cr_container, R.id.trial_math_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trial_sc_container:
                startDeal(getString(R.string.str_free_one_type), TRIAL_SC);
                break;
            case R.id.trial_rc_container:
                startDeal(getString(R.string.str_free_two_type), TRIAL_RC);
                break;
            case R.id.trial_cr_container:
                startDeal(getString(R.string.str_free_thr_type), TRIAL_CR);
                break;
            case R.id.trial_math_container:
                startDeal(getString(R.string.str_free_four_type), TRIAL_MATH);
                break;
            default:
                break;
        }
    }

}
