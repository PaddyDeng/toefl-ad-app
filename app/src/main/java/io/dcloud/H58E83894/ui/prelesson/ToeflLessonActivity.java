package io.dcloud.H58E83894.ui.prelesson;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.BaseTabActivity;
import io.dcloud.H58E83894.base.adapter.TabPagerAdapter;
import io.dcloud.H58E83894.ui.prelesson.toeflLesson.AttemptLessonFragment;
import io.dcloud.H58E83894.ui.prelesson.toeflLesson.ClassroomTechFragment;
import io.dcloud.H58E83894.ui.prelesson.toeflLesson.LiveLessonFragment;
import io.dcloud.H58E83894.ui.prelesson.toeflLesson.VideoLessonFragment;
import io.dcloud.H58E83894.weiget.setIndicator;

public class ToeflLessonActivity extends BaseTabActivity {



//    @BindView(R.id.fixed_community_tablayout)
//    TabLayout mTabLayout;
//    @BindView(R.id.fixed_community_viewpager)
    ViewPager mViewPager;
    private List<Fragment> list;
    private int levelId;
    private String title;

    @BindView(R.id.diff_detail_title_tv)
    TextView titleTxt;
    private static int TAB_MARGIN_DIP = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toefl_lesson);
    }

    @Override
    protected void initView() {
        titleTxt.setText(getResources().getText(R.string.str_infor_loads));

        initLayoutViews(R.id.tabs, R.id.viewpager);
    }

    protected void initLayoutViews(@IdRes int tabResId, @IdRes int vpResId) {
        tabLayout = (TabLayout) findViewById(tabResId);
        viewPager = (ViewPager) findViewById(vpResId);
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        setIndicator.setIndicator(this, tabLayout, TAB_MARGIN_DIP, TAB_MARGIN_DIP);
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.divider)); //设置分割线的样式
        linearLayout.setDividerPadding(dip2px(10)); //设置分割线间隔
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


    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(new LiveLessonFragment());
        fragList.add(new ClassroomTechFragment());
        fragList.add(new VideoLessonFragment());
        fragList.add(new AttemptLessonFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_name)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }


}
