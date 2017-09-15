package io.dcloud.H58E83894.base;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by fire on 2017/7/14  11:13.
 */

public abstract class BaseTabFragment extends BaseFragment {

    protected void initTabAdapter(ViewPager mViewPager, TabLayout mTabLayout) {
        mViewPager.setAdapter(getPagerAdapter());
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }


    protected abstract PagerAdapter getPagerAdapter();

}
