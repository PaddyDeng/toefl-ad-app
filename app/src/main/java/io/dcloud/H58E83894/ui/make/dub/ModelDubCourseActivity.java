package io.dcloud.H58E83894.ui.make.dub;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.dub.adapter.DubAdapter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ModelDubCourseActivity extends BaseActivity {

    @BindView(R.id.hot_recycler)
    RecyclerView hotRecycler;//配音推荐
    private boolean asynData;
    private PreLessonData mPreLessonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_mo_dub);
        asynBannerData();

//        mPullRefreshLayout.setOnRefreshListener(this);
//        if (adapter == null) {
//            initNewsList();
//        }
//        adapter.onShowInitView(true);
//        asyncRequest();
    }



    private void asynBannerData() {
        addToCompositeDis(HttpUtil.preLesson()
                .subscribe(new Consumer<PreLessonData>() {
                    @Override
                    public void accept(@NonNull PreLessonData data) throws Exception {
                        asynData = true;
                        mPreLessonData = data;
                        initHotData(mPreLessonData.getHotData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    private void initHotData(final List<LessonData> data) {//热门模块
//        LinearLayout
//        hotRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
//                mPullRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
//            }
//        });

        hotRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        DubAdapter hotAdapter = new DubAdapter(data);
        hotAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonData hotData = data.get(position);
                forword(DubDetailActivity.class);
            }
        });
        hotRecycler.setNestedScrollingEnabled(false);
        hotRecycler.setAdapter(hotAdapter);
    }





    @OnClick({R.id.add_dub_course})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_dub_course:  //添加课程
//                forword(GrammarTestActivity.class);
                break;
            default:
                break;
        }
    }
}
