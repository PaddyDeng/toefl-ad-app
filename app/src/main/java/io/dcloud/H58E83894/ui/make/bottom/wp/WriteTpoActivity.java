package io.dcloud.H58E83894.ui.make.bottom.wp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseTabActivity;
import io.dcloud.H58E83894.base.adapter.TabPagerAdapter;


/**
 * @Deil 写作练习
 * */
public class WriteTpoActivity extends BaseTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tpo);
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
        final String[] array = getResources().getStringArray(R.array.toefl_write_tpo_arr);
        fragList.add(WriteTpoListFragment.getInstance(1, 6));
        fragList.add(WriteTpoListFragment.getInstance(7, 6));
        fragList.add(WriteTpoListFragment.getInstance(13, 6));
        fragList.add(WriteTpoListFragment.getInstance(19, 6));
        fragList.add(WriteTpoListFragment.getInstance(25, 6));
        fragList.add(WriteTpoListFragment.getInstance(31, 4));
        fragList.add(WriteTpoListFragment.getInstance(40, 5));
        fragList.add(WriteTpoListFragment.getInstance(45, 6));

        return new TabPagerAdapter(getSupportFragmentManager(), array) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
