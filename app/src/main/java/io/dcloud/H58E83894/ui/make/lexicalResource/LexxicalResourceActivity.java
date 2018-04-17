package io.dcloud.H58E83894.ui.make.lexicalResource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseTabActivity;
import io.dcloud.H58E83894.base.adapter.TabPagerAdapter;

public class LexxicalResourceActivity extends BaseTabActivity {

    public static void startGrammarTestActivity(Context mContext) {
        Intent intent = new Intent(mContext, LexxicalResourceActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, true);
        mContext.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lexical_re);

    }

    @Override
    protected void initView() {
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(new TodayCorretFragment());
        fragList.add(new PastCorretFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.toefl_speak_corret)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }

}
