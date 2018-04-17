package io.dcloud.H58E83894.ui.make.dub;

import android.os.Bundle;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;

public class DubMyPraciseActivity extends BaseActivity {

//    @BindView(R.id.sum_recycler)
//    RecyclerView sumRecycler;//人数
//    @BindView(R.id.rank_recycler)
//    RecyclerView rankRecycler;//排行榜
//    private boolean asynData;
//    private PreLessonData mPreLessonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dub_my_pra);
//        asynBannerData();

//        mPullRefreshLayout.setOnRefreshListener(this);
//        if (adapter == null) {
//            initNewsList();
//        }
//        adapter.onShowInitView(true);
//        asyncRequest();
    }



//    private void asynBannerData() {
//        addToCompositeDis(HttpUtil.preLesson()
//                .subscribe(new Consumer<PreLessonData>() {
//                    @Override
//                    public void accept(@NonNull PreLessonData data) throws Exception {
//                        asynData = true;
//                        mPreLessonData = data;
//                        initHotData(mPreLessonData.getHotData());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                    }
//                }));
//    }
//
//    private void initHotData(final List<LessonData> data) {//热门模块
////        LinearLayout
////        hotRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
////            @Override
////            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
////                super.onScrollStateChanged(recyclerView, newState);
////            }
////
////            @Override
////            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
////                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
////                mPullRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
////            }
////        });
//
//        rankRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        DubDeRankAdapter hotAdapter = new DubDeRankAdapter(data);
//        hotAdapter.setItemListener(new OnItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                LessonData hotData = data.get(position);
//                ToeflDetailActivity.startToeflDetail(DubMyPraciseActivity.this, hotData, C.TYPE_HOT_LESSON);
//            }
//        });
////        rankRecycler.setNestedScrollingEnabled(false);
//        rankRecycler.setAdapter(hotAdapter);
//
//        sumRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        DubDeSumAdapter SumAdapter = new DubDeSumAdapter(data);
//        hotAdapter.setItemListener(new OnItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                LessonData hotData = data.get(position);
//                ToeflDetailActivity.startToeflDetail(DubMyPraciseActivity.this, hotData, C.TYPE_HOT_LESSON);
//            }
//        });
////        rankRecycler.setNestedScrollingEnabled(false);
//        sumRecycler.setAdapter(SumAdapter);
//
//    }
//
//
//
//
//
//    @OnClick({R.id.add_dub_course})
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.add_dub_course:  //我要练习
////                forword(GrammarTestActivity.class);
//                break;
//            default:
//                break;
//        }
//    }
}
