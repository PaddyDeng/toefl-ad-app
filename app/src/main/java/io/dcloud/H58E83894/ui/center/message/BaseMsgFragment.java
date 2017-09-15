package io.dcloud.H58E83894.ui.center.message;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.MsgData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/11  13:43.
 */

public abstract class BaseMsgFragment extends BaseRefreshFragment<MsgData> {

    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());

    @Override
    protected BaseRecyclerViewAdapter<MsgData> getAdapter() {
        return new MsgAdapter(getActivity(), null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    protected void asyncData(String page, final boolean isRefresh) {
        addToCompositeDis(HttpUtil.getMsg(getType(), page).subscribe(new Consumer<ResultBean<List<MsgData>>>() {
            @Override
            public void accept(@NonNull ResultBean<List<MsgData>> bean) throws Exception {
                List<MsgData> data = bean.getData();
                if (data == null || data.isEmpty()) {
                    if (isRefresh) {
                        updateRecycleView(null, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
                    } else {
                        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                    }
                } else {
                    if (isRefresh) {
                        updateRecycleView(data, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_SUCCESS);
                    } else {
                        updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                if (isRefresh) {
                    updateRecycleView(null, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
                } else {
                    updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
                }
            }
        }));
    }

    protected abstract String getType();
}
