package io.dcloud.H58E83894.ui.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseTabActivity;
import io.dcloud.H58E83894.base.adapter.TabPagerAdapter;
import io.dcloud.H58E83894.weiget.setIndicator;


/**
 *
 * 知识库专区
 * */
public class KnowPrefectureActivity extends BaseTabActivity {


//    @BindView(R.id.share_know)
//    ImageView shareKnow;

    public static void KnowPrefecture(Context c, int index, String titleName) {
        Intent intent = new Intent(c, KnowPrefectureActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, titleName);
        c.startActivity(intent);
    }
    ViewPager mViewPager;
    private List<Fragment> list;
    private int levelId;
    private String title;

    @BindView(R.id.diff_detail_title_tv)
    TextView titleTxt;
    private static int TAB_MARGIN_DIP = 24;
    private int index;
    private String titleName;
    @Override
    protected void getArgs() {
        super.getArgs();

        index = getIntent().getIntExtra(Intent.EXTRA_INDEX, 0);
        titleName = getIntent().getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
    }

    private  KnowFreeFragment knowFreeFragment;
    private  KnowLeiZoneFragment knowLeiZoneFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toefl_lesson);
    }

    @Override
    protected void initView() {
        if(TextUtils.isEmpty(titleName)){
            return;
        }
        titleTxt.setText(titleName);
//        shareKnow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareDialog().showDialog(getSupportFragmentManager());
//            }
//        });
        initLayoutViews(R.id.tabs, R.id.viewpager);
            }

    protected void initLayoutViews(@IdRes int tabResId, @IdRes int vpResId) {
        tabLayout = (TabLayout) findViewById(tabResId);
        viewPager = (ViewPager) findViewById(vpResId);
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        setIndicator.setIndicator(this, tabLayout, TAB_MARGIN_DIP, TAB_MARGIN_DIP);
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
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




    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(KnowFreeFragment.newInstance(index));
        fragList.add(KnowLeiZoneFragment.newInstance(index));
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.knowledge_zone)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        knowFreeFragment.onActivityResult(requestCode, resultCode, data);
        knowLeiZoneFragment.onActivityResult(requestCode, resultCode, data);

    }

}
