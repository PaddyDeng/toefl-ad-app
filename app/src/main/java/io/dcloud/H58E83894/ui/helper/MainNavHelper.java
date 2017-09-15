package io.dcloud.H58E83894.ui.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;

import io.dcloud.H58E83894.MainActivity;

/**
 * Created by fire on 2017/7/12.
 */

public class MainNavHelper {
    private int CONTAINER;
    private MainActivity mMainActivity;
    private SparseArray<Fragment> naviMapFragment = new SparseArray<>();

    public SparseArray<Fragment> getNaviMapFragment() {
        return naviMapFragment;
    }

    public MainNavHelper(MainActivity activity, int CONTAINER) {
        this.CONTAINER = CONTAINER;
        mMainActivity = activity;
    }

    public void replaceFragment(FragmentManager fm, int viewId) {
        String tag = String.valueOf(viewId);
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.findFragmentByTag(tag) == null) {
            transaction.replace(CONTAINER, naviMapFragment.get(viewId), tag);
            transaction.addToBackStack(tag);
        } else {
            transaction.replace(CONTAINER, fm.findFragmentByTag(tag), tag);
        }
        transaction.commitAllowingStateLoss();
        //
        for (int i = 0, size = naviMapFragment.size(); i < size; i++) {
            int curId = naviMapFragment.keyAt(i);
            if (curId == viewId) {
                mMainActivity.findViewById(viewId).setSelected(true);
            } else {
                mMainActivity.findViewById(curId).setSelected(false);
            }
        }
    }

    public void switchFragment(FragmentManager fm,View view) {
        int viewId = view.getId();
        if (naviMapFragment.indexOfKey(viewId) < 0) return;
        if (!view.isSelected()) {
            replaceFragment(fm, viewId);
        }
    }

    public void addFragment(Fragment fragment, int id) {
        View view = mMainActivity.findViewById(id);
        view.setSelected(false);
        naviMapFragment.put(id, fragment);
    }
}
