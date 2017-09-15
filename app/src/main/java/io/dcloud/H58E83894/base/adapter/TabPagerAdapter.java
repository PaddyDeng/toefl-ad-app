package io.dcloud.H58E83894.base.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class TabPagerAdapter extends FragmentPagerAdapter {
    private List<String> titles;

    public TabPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = Arrays.asList(titles);
    }


    public TabPagerAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        this.titles = titles;
    }


    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
