package io.dcloud.H58E83894.ui.center.record.write;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.record.WriteRecordData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.record.adapter.WriteRecordAdapter;
import io.dcloud.H58E83894.ui.make.bottom.wp.WriteResultActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/7  13:41.
 */

public abstract class BaseWriteRecordFragment extends BaseRefreshFragment<WriteRecordData> {

    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
    private int recordWriteNum;
    private Observable<Integer> mObservable;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mObservable = RxBus.get().register(C.REFRESH_WRITE_LIST, Integer.class);
        mObservable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (recordWriteNum == integer) {
                    asyncRequest();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mObservable != null) {
            RxBus.get().unregister(C.REFRESH_WRITE_LIST, mObservable);
        }
    }

    @Override
    protected BaseRecyclerViewAdapter<WriteRecordData> getAdapter() {
        return new WriteRecordAdapter(getActivity(), null, mManager);
    }

    @Override
    protected void setListener(List<WriteRecordData> data, int position) {
        super.setListener(data, position);
        WriteRecordData recordData = data.get(position);
        recordWriteNum = Integer.parseInt(recordData.getContentId());
        WriteResultActivity.startResult(getActivity(), recordWriteNum, recordData.getContentId(), recordData.getBelong());
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    protected void asyncData(final int page, final boolean refresh) {
        addToCompositeDis(HttpUtil
                .writeRecord(getReadType(), String.valueOf(page))
                .subscribe(new Consumer<ResultBean<List<WriteRecordData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<WriteRecordData>> bean) throws Exception {
                        List<WriteRecordData> data = bean.getData();
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
