package io.dcloud.H58E83894.ui.center.know;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.lesson.LessonCenterAdapter;
import io.dcloud.H58E83894.ui.prelesson.MaryListActivity;
import io.dcloud.H58E83894.ui.toeflcircle.ExamInfoFragment;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MyKnowActivity extends BaseActivity {




    @BindView(R.id.deal_title)
    TextView titleTxt;

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(mContext);



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_know);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    @Override
    protected void initData() {
        titleTxt.setText("我的知识库");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
            Fragment myfragment03 = new MyKnowLeiZoneFragment();
            transaction.replace(R.id.frame, myfragment03);
            transaction.commit();
    }

}
