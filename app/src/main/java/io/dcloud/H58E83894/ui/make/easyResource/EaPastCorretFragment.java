package io.dcloud.H58E83894.ui.make.easyResource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.ui.make.easyResource.fragment.EaOnlyMineFragment;
import io.dcloud.H58E83894.ui.make.easyResource.fragment.EaSeeAllFragment;
import io.dcloud.H58E83894.ui.make.lexicalResource.fragment.OnlyMineFragment;
import io.dcloud.H58E83894.ui.make.lexicalResource.fragment.SeeAllFragment;

/**
 * Created by fire on 2017/7/25  11:54.
 */

public class EaPastCorretFragment extends BaseFragment {

    @BindView(R.id.frame)
    FrameLayout frame;
    @BindView(R.id.past_see_all)
    TextView seeAll;
    @BindView(R.id.past_see_mine)
    TextView seeMine;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_make_past, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        seeAll.setTextColor(getResources().getColor(R.color.color_text_green));
        seeMine.setTextColor(getResources().getColor(R.color.color_text_black));
        EaSeeAllFragment myfragment = new EaSeeAllFragment();
        transaction.replace(R.id.frame, myfragment);
        transaction.commit();
    }

    @OnClick({R.id.past_see_all, R.id.past_see_mine})
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.past_see_all:
                seeAll.setTextColor(getResources().getColor(R.color.color_text_green));
                seeMine.setTextColor(getResources().getColor(R.color.color_text_black));
                EaSeeAllFragment myfragment = new EaSeeAllFragment();
                transaction.replace(R.id.frame, myfragment);
                transaction.commit();
                break;
            case R.id.past_see_mine:
                seeMine.setTextColor(getResources().getColor(R.color.color_text_green));
                seeAll.setTextColor(getResources().getColor(R.color.color_text_black));
                EaOnlyMineFragment myfragment01 = new EaOnlyMineFragment();
                transaction.replace(R.id.frame, myfragment01);
                transaction.commit();
                break;

        }
    }
}