package io.dcloud.H58E83894.ui.make.dub;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.dub.adapter.DubDeRankAdapter;
import io.dcloud.H58E83894.ui.make.dub.adapter.DubDeSumAdapter;
import io.dcloud.H58E83894.ui.prelesson.ToeflDetailActivity;
import io.dcloud.H58E83894.utils.C;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class DubDetailActivity extends BaseActivity {

    @BindView(R.id.sum_recycler)
    RecyclerView sumRecycler;//人数
    @BindView(R.id.rank_recycler)
    RecyclerView rankRecycler;//排行榜
    @BindView(R.id.dub_up)
    ImageView dubUp;
    @BindView(R.id.linear_dub)
    LinearLayout linearDub;
    private boolean isVisible = true;
    private boolean asynData;
    private PreLessonData mPreLessonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_dub_de);
        asynBannerData();

//        mPullRefreshLayout.setOnRefreshListener(this);
//        if (adapter == null) {
//            initNewsList();
//        }
//        adapter.onShowInitView(true);
//        asyncRequest();
        initUp();
    }

    private void initUp() {//隐藏布局
        dubUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    dubUp.setImageDrawable(getResources().getDrawable(R.drawable.ic_dub_up));
                    linearDub.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    dubUp.setImageDrawable(getResources().getDrawable(R.drawable.ic_dub_down));
                    linearDub.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
        });
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

        rankRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DubDeRankAdapter hotAdapter = new DubDeRankAdapter(data);
        hotAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonData hotData = data.get(position);
                ToeflDetailActivity.startToeflDetail(DubDetailActivity.this, hotData, C.TYPE_HOT_LESSON);
            }
        });
//        rankRecycler.setNestedScrollingEnabled(false);
        rankRecycler.setAdapter(hotAdapter);

        sumRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        DubDeSumAdapter SumAdapter = new DubDeSumAdapter(data);
        hotAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonData hotData = data.get(position);
                ToeflDetailActivity.startToeflDetail(DubDetailActivity.this, hotData, C.TYPE_HOT_LESSON);
            }
        });
//        rankRecycler.setNestedScrollingEnabled(false);
        sumRecycler.setAdapter(SumAdapter);

    }





    @OnClick({R.id.add_dub_course})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_dub_course:  //我要练习
//                forword(GrammarTestActivity.class);
                break;
            default:
                break;
        }
    }
}
