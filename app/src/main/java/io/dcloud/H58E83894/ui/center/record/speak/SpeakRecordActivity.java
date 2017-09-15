package io.dcloud.H58E83894.ui.center.record.speak;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseTabActivity;
import io.dcloud.H58E83894.base.adapter.TabPagerAdapter;

public class SpeakRecordActivity extends BaseTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_record);
    }

    @Override
    protected void initView() {
        initLayoutView(R.id.tabs, R.id.viewpager);
    }


    @Override
    protected void setTabModeAndGravity(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(new SpeakOneFragment());
        fragList.add(new SpeakTwoFragment());
        fragList.add(new SpeakThrFragment());
        fragList.add(new SpeakFourFragment());
        fragList.add(new SpeakFiveFragment());
        fragList.add(new SpeakSixFragment());
        fragList.add(new SpeakGlodFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.center_speak_record_arr)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
