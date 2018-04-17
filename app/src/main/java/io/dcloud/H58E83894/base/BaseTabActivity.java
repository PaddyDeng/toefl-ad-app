package io.dcloud.H58E83894.base;

import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import io.dcloud.H58E83894.R;

public abstract class BaseTabActivity extends BaseActivity {
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected int currentPage = 0;

    protected void initLayoutView(@IdRes int tabResId, @IdRes int vpResId) {
        tabLayout = (TabLayout) findViewById(tabResId);
        viewPager = (ViewPager) findViewById(vpResId);
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
//                R.drawable.divider)); //设置分割线的样式
//        linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
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
    //像素单位转换
    public int dip2px(int dip) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }

    protected void setTabModeAndGravity(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果
    }

    protected abstract PagerAdapter getPagerAdapter();
}
