package io.dcloud.H58E83894.ui.make.everyDayListen;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.BaseTabActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.TabPagerAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.MyLessonData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.lesson.LessonCenterAdapter;
import io.dcloud.H58E83894.ui.center.record.read.ReadAllRecordFragment;
import io.dcloud.H58E83894.ui.center.record.read.ReadOgRecordFragment;
import io.dcloud.H58E83894.ui.center.record.read.ReadTpoRecordFragment;
import io.dcloud.H58E83894.ui.prelesson.toeflLesson.AttemptLessonFragment;
import io.dcloud.H58E83894.ui.prelesson.toeflLesson.ClassroomTechFragment;
import io.dcloud.H58E83894.ui.prelesson.toeflLesson.LiveLessonFragment;
import io.dcloud.H58E83894.ui.prelesson.toeflLesson.VideoLessonFragment;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.setIndicator;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MyEveListenLessonActivity extends BaseTabActivity {

    @BindView(R.id.title_tv_style)
    TextView titleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_record);
    }

    @Override
    protected void initView() {

        titleName.setText("趣味听力");
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(new MyListenAllRecordFragment());
        fragList.add(new MyListenCollectionRecordFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.center_collection_record)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
