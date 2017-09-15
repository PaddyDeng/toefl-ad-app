package io.dcloud.H58E83894.ui.center.record.read;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.record.ReadRecordData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.record.adapter.ReadRecordAdapter;
import io.dcloud.H58E83894.ui.make.bottom.rp.ReadQuestionActivity;
import io.dcloud.H58E83894.ui.make.bottom.rp.ReadResultActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/7  09:19.
 */

public abstract class BaseReadRecordFragment extends BaseRefreshFragment<ReadRecordData> {

    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
    private Observable<String> refObs;
    private String recordClickId;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refObs = RxBus.get().register(C.REFRESH_READ_LIST, String.class);
        refObs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                if (TextUtils.equals(recordClickId, s)) {
                    asyncRequest();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refObs != null) {
            RxBus.get().unregister(C.REFRESH_READ_LIST, refObs);
        }
    }

    @Override
    protected BaseRecyclerViewAdapter<ReadRecordData> getAdapter() {
        return new ReadRecordAdapter(getActivity(), null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void setListener(List<ReadRecordData> data, int position) {
        super.setListener(data, position);
        ReadRecordData recordData = data.get(position);
        recordClickId = recordData.getCpid();
        int record = recordData.getUserAnswerRecord();
        int son = recordData.getSon();
        if (record == son) {
            ReadResultActivity.startReadResultAct(getActivity(), recordData.getCpid(), recordData.getBelong());
        } else {
            ReadQuestionActivity.startReadQuestionAct(getActivity(), recordData.getCpid(), recordData.getBelong());
        }
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
    }

    protected void asyncData(final int page, final boolean refresh) {
        addToCompositeDis(HttpUtil
                .readRecord(getReadType(), String.valueOf(page))
                .subscribe(new Consumer<ResultBean<List<ReadRecordData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<ReadRecordData>> bean) throws Exception {
                        List<ReadRecordData> data = bean.getData();
                        if (data == null || data.isEmpty()) {
                            if (refresh) {
                                updateRecycleView(null, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
                            } else {
                                updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            if (refresh) {
                                updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        setPage(page);
                        if (refresh) {
                            updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_REFRESH_FAIL);
                        } else {
                            updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
                        }
                    }
                }));
    }

    protected abstract String getReadType();

    //加载失败，重置page
    protected abstract void setPage(int page);
}
