package io.dcloud.H58E83894.ui.prelesson.toeflLesson;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.FreeCursorData;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.data.prelesson.ToeflLessonData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.ui.prelesson.ToeflDetailActivity;
import io.dcloud.H58E83894.ui.prelesson.adapter.ToeflLessonAdapter;
import io.dcloud.H58E83894.ui.prelesson.adapter.ToeflLessonsAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RecyclerViewClickListener;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public  class AttemptLessonFragment extends BaseFragment {

    private PreLessonData mPreLessonData;
    private boolean asyncFreeData;
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
        if (!asyncFreeData) {
            asynBannerData();
        }

    }

    private void asynBannerData() {
        addToCompositeDis(HttpUtil
                .getFreeCursor()
                .subscribe(
                        new Consumer<List<FreeCursorData>>() {
                            @Override
                            public void accept(@NonNull List<FreeCursorData> datas) throws Exception {
                                initOnlineData(datas);
                                asyncFreeData = true;
                            }
                        }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
    }


    private void initOnlineData(final List<FreeCursorData> datas) {//托福模块
        lessonRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ToeflLessonsAdapter lessonAdapter = new ToeflLessonsAdapter(datas);
        lessonRecycler.addOnItemTouchListener(new RecyclerViewClickListener(getContext(), lessonRecycler,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FreeCursorData data = datas.get(position);
                        String url = data.getUrl();
                        Log.i("kkkll", url.toString());
                        String titleName = data.getName();
                        if (TextUtils.isEmpty(url))
                            return;
                        DealActivity.startDealActivity(getActivity(), titleName, url);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

//        lessonAdapter.setItemListener(new OnItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//            }
//        });
        lessonRecycler.setAdapter(lessonAdapter);
    }
}
