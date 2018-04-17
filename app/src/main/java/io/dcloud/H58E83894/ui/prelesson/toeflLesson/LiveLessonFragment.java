package io.dcloud.H58E83894.ui.prelesson.toeflLesson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caimuhao.rxpicker.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.data.prelesson.ToeflLessonData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.prelesson.ToeflDetailActivity;
import io.dcloud.H58E83894.ui.prelesson.adapter.ToeflLessonAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RecyclerViewClickListener;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


public  class LiveLessonFragment extends BaseFragment {

    private PreLessonData mPreLessonData;
    private boolean asynData;
    @BindView(R.id.recycler_view)
    RecyclerView lessonRecycler;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_base, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!asynData) {
            asynBannerData();
        }

    }

    private void asynBannerData() {
        addToCompositeDis(HttpUtil.preLesson()
                .subscribe(new Consumer<PreLessonData>() {
                    @Override
                    public void accept(@NonNull PreLessonData data) throws Exception {
                        asynData = true;
                        mPreLessonData = data;
                        refreshUi();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    @Override
    protected void refreshUi() {
        if (mPreLessonData == null) return;
        Log.i("ppp3", mPreLessonData.getHotData().toString());
        Log.i("ppp4", mPreLessonData.getDataCourse().toString());

        initOnlineData(mPreLessonData.getDataCourse());
    }

    private void initOnlineData(ToeflLessonData data) {//托福模块
        lessonRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        final List<LessonData> allData = new ArrayList<>();
        allData.addAll(data.get直播课程());
        ToeflLessonAdapter lessonAdapter = new ToeflLessonAdapter(allData);
        lessonRecycler.addOnItemTouchListener(new RecyclerViewClickListener(getContext(), lessonRecycler,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        LessonData lessonData = allData.get(position);
                     Log.i("ssssss", lessonData.toString());
                      ToeflDetailActivity.startToeflDetail(getActivity(), lessonData, C.TYPE_ONLINE_LESSON);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));


        Log.i("ssssss1", data.toString());
//        lessonAdapter.setItemListener(new OnItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                LessonData lessonData = allData.get(position);
//                Log.i("ssssss", lessonData.toString());
//                ToeflDetailActivity.startToeflDetail(getActivity(), lessonData, C.TYPE_ONLINE_LESSON);
//            }
//        });
        lessonRecycler.setAdapter(lessonAdapter);
    }
}
