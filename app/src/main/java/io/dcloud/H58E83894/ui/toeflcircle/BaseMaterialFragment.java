package io.dcloud.H58E83894.ui.toeflcircle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;



import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.prelesson.adapter.DownloadAdapter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public abstract class BaseMaterialFragment extends BaseRefreshFragment<DownloadData> {

    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());

    protected void asyncData(final boolean refresh,  int pages) {
        addToCompositeDis(HttpUtil.MaryList(getSelectId(), pages)
                .subscribe(new Consumer<ResultBean<List<DownloadData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<DownloadData>> bean) throws Exception {
                        List<DownloadData> data = bean.getData();
                        if (data != null && !data.isEmpty()) {
                            if (refresh) {
                                updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            asyncFail(refresh, bean.getMessage(), true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        asyncFail(refresh, errorTip(throwable), false);
                    }
                }));
    }

    private void asyncFail(boolean refresh, String msg, boolean loadBoo) {
        if (refresh) {
            updateRecycleView(null, msg, loadBoo ? InitDataType.TYPE_REFRESH_SUCCESS : InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, msg, loadBoo ? InitDataType.TYPE_LOAD_MORE_SUCCESS : InitDataType.TYPE_LOAD_MORE_FAIL);
        }
        if (!loadBoo) {
            asyncFail();
        }
    }

    protected abstract void asyncFail();

    @Override
    public BaseRecyclerViewAdapter<DownloadData> getAdapter() {
        return new DownloadAdapter(getActivity(), null, mManager, getIndex());
    }

    @Override
    public void setListener(List<DownloadData> data, int position) {
        super.setListener(data, position);
        DownloadData downloadData = data.get(position);
        if (downloadData != null) {
            CommunitysDetailActivity.startCommunity(getActivity(), downloadData.getId());
        }
    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_height_divider));
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    public abstract int getSelectId();

    public abstract int getIndex();
}
