package io.dcloud.H58E83894.ui.make.bottom.sp;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.adapter.SpeakGlodAdapter;
import io.dcloud.H58E83894.utils.C;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class SpeakGlodListActivity extends BaseListActivity<PracticeData> {

    private int page;
    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(this);

    @Override
    protected void initView() {
        baseTitleTxt.setText(R.string.str_glod_speak_practice_title);
    }

    @Override
    protected void asyncLoadMore() {
        page++;
        asyncData(false);
    }

    @Override
    protected BaseRecyclerViewAdapter<PracticeData> getAdapter() {
        return new SpeakGlodAdapter(mContext, null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        asyncData(true);
    }

    private void asyncData(final boolean isRefresh) {
        addToCompositeDis(HttpUtil
                .goldSpeak(String.valueOf(page))
                .subscribe(new Consumer<List<PracticeData>>() {
                    @Override
                    public void accept(@NonNull List<PracticeData> data) throws Exception {
                        if (isRefresh) {
                            if (data != null && !data.isEmpty()) {
                                updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                page--;
                                updateRecycleView(null, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            }
                        } else {
                            if (data != null && !data.isEmpty()) {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            } else {
                                page--;
                                updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        page--;
                        if (isRefresh) {
                            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_REFRESH_FAIL);
                        } else {
                            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
                        }
                    }
                }));
    }

    @Override
    protected void setListener(List<PracticeData> data, int position) {
        PracticeData practiceData = data.get(position);
        SpeakQuestionActivity.startSpeakQuestionAct(mContext, practiceData.getId(), C.TYPE_SPEAK_GLOD);
    }
}
