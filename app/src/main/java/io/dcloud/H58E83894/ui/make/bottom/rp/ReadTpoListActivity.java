package io.dcloud.H58E83894.ui.make.bottom.rp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseTabActivity;
import io.dcloud.H58E83894.base.adapter.TabPagerAdapter;
import io.dcloud.H58E83894.utils.Utils;

public class ReadTpoListActivity extends BaseTabActivity {
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private int heigth;
    private boolean init;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_tpo_list);
    }

    @Override
    protected void initView() {
        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mViewPager,this);
                if(!init){
                    init = true;
                    heigth = mViewPager.getMeasuredHeight();
                    initLayoutView(R.id.tabs, R.id.viewpager);
                }
            }
        });
    }

    @Override
    protected void setTabModeAndGravity(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        final List<String> titles = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            int newValue = cacl(i);
            if (i == newValue) {
                continue;
            }
            StringBuffer sb = new StringBuffer();
            if (i < 10) {
                sb.append(0);
            }
            sb.append(i);
            titles.add(getString(R.string.str_read_tpo_title, sb.toString()));
            fragList.add(ReadTpoFragment.getInstance(newValue,heigth));
        }
        return new TabPagerAdapter(getSupportFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }

    private int cacl(int tpoNumber) {
        if (tpoNumber < 35) {
            tpoNumber += 187;
        } else if (tpoNumber < 40) {

        } else if (tpoNumber < 49) {
            tpoNumber += 223;
        } else if (tpoNumber < 50) {
            tpoNumber += 265;
        }
        return tpoNumber;
    }
}
