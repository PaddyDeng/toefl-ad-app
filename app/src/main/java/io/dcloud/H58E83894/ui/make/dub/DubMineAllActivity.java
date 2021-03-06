package io.dcloud.H58E83894.ui.make.dub;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.dub.adapter.DubAdapter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * 她的配音作品
 * */
public class DubMineAllActivity extends BaseActivity {

    @BindView(R.id.mine_all)
    RecyclerView mineAllRecy;//全部作品
    private boolean asynData;
    private PreLessonData mPreLessonData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dub_mine);
//        asynBannerData();

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
        mineAllRecy.setLayoutManager(new GridLayoutManager(this, 2));
        DubAdapter hotAdapter = new DubAdapter(data);
        hotAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonData hotData = data.get(position);
                forword(ModelDubCourseActivity.class);
//                ToeflDetailActivity.startToeflDetail(AllDubCourseActivity.this, hotData, C.TYPE_HOT_LESSON);
            }
        });
        mineAllRecy.setAdapter(hotAdapter);
    }

}
