package io.dcloud.H58E83894.ui.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.ViewPagerFragmentAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.guide_viwepager)
    ViewPager mViewPager;
    @BindView(R.id.guide_jump_tv)
    TextView jumpTv;
    private int[] dRes = new int[]{
            R.drawable.icon_guide_fir,
            R.drawable.icon_guide_sec,
            R.drawable.icon_guide_thr
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        List<Fragment> lf = new ArrayList<>();
        for (int i = 0; i < dRes.length; i++) {
            lf.add(GuideDetailPager.getInstance(dRes[i]));
        }
        mViewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), lf));
        addListener();
    }

    private void addListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == dRes.length - 1)
                    Utils.setVisible(jumpTv);
                else
                    Utils.setGone(jumpTv);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.guide_jump_tv)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guide_jump_tv:
                goMainAct();
                break;
            default:
                break;
        }
    }

    private void goMainAct() {
        SharedPref.setGuideInfo(mContext, C.CONT_GUIDE_IMG);
        forword(MainActivity.class);
        finishWithAnim();
    }
}
