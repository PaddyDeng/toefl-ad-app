package io.dcloud.H58E83894.base;

import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public abstract class BaseTabActivity extends BaseActivity {
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected int currentPage = 0;

    protected void initLayoutView(@IdRes int tabResId, @IdRes int vpResId) {
        tabLayout = (TabLayout) findViewById(tabResId);
        viewPager = (ViewPager) findViewById(vpResId);
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        setTabModeAndGravity(tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void setTabModeAndGravity(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果
    }

    protected abstract PagerAdapter getPagerAdapter();
}
