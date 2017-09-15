package io.dcloud.H58E83894.ui.center.record.listen;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/11  17:14.
 */

public abstract class BaseListenRecordFragment<T, V> extends BaseRefreshFragment<T> {

    protected LinearLayoutManager mManager = new LinearLayoutManager(getActivity());

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    protected void asyncData(final boolean isRefresh) {
        addToCompositeDis(getObservable().subscribe(new Consumer<V>() {
            @Override
            public void accept(@NonNull V ts) throws Exception {
                requestSuccess(ts, isRefresh);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                if (isRefresh) {
                    updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_REFRESH_FAIL);
                } else {
                    updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
                }
            }
        }));
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        super.initRecyclerViewItemDecoration(mRecyclerView);
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
    }

    protected void refreshUi(List<T> data, boolean refresh) {
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

    protected abstract void requestSuccess(V ts, boolean refresh);

    protected abstract Observable<V> getObservable();

}
