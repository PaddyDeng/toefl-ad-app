package io.dcloud.H58E83894.ui.make.bottom.rp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.data.make.ReadOgData;
import io.dcloud.H58E83894.data.make.ReadOgQuestion;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.adapter.ReadOgAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ReadOgListActivity extends BaseListActivity<PracticeData> {

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(this);
    private Observable<String> refObs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refObs = RxBus.get().register(C.REFRESH_READ_LIST, String.class);
        refObs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                asyncRequest();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refObs != null) {
            RxBus.get().unregister(C.REFRESH_READ_LIST, refObs);
        }
    }

    @Override
    protected void initView() {
        baseTitleTxt.setText(R.string.str_read_practice_og_title);
    }


    @Override
    protected void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    protected BaseRecyclerViewAdapter<PracticeData> getAdapter() {
        return new ReadOgAdapter(mContext, null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void setListener(List<PracticeData> data, int position) {
        super.setListener(data, position);
        if (needLogin()) {
            return;
        }
        PracticeData practiceData = data.get(position);
        if (practiceData.getFinish() == 0 || practiceData.getFinish() == 1) {
            int index = position + 1;
            StringBuffer sb = new StringBuffer();
            if (index <= 9) {
                sb.append(0);
            }
            sb.append(index);
            ReadQuestionActivity.startReadQuestionAct(mContext, practiceData.getId(), C.READ_BELONG_OG);
        } else {//去结果页
            ReadResultActivity.startReadResultAct(mContext, practiceData.getId(), C.READ_BELONG_OG);
        }
    }

    @Override
    protected void asyncRequest() {
        addToCompositeDis(HttpUtil
                .readOg()
                .subscribe(new Consumer<ReadOgData>() {
                    @Override
                    public void accept(@NonNull ReadOgData data) throws Exception {
                        List<ReadOgQuestion> dataOG = data.getOG();
                        if (dataOG == null || dataOG.isEmpty()) {
                            updateRecycleView(null, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            return;
                        }
                        ReadOgQuestion og = dataOG.get(0);
                        if (og == null) return;
                        List<PracticeData> question = og.getQuestion();
                        if (question != null && !question.isEmpty()) {
                            updateRecycleView(question, "", InitDataType.TYPE_REFRESH_SUCCESS);
                        } else {
                            updateRecycleView(null, "", InitDataType.TYPE_REFRESH_SUCCESS);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
    }

}
