package io.dcloud.H58E83894.ui.center.record.speak;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.SpeakShare;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.record.adapter.SpeakRecordAdapter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/15  11:07.
 */

public abstract class BaseSpeakRecordFragment extends BaseRefreshFragment<SpeakShare> {

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(getActivity());

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        super.initRecyclerViewItemDecoration(mRecyclerView);
<<<<<<< HEAD
        mRecyclerView.addItemDecoration(
                new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
=======
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    }

    @Override
    protected BaseRecyclerViewAdapter<SpeakShare> getAdapter() {
        return new SpeakRecordAdapter(getActivity(), null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    protected void asyncData(int page, final boolean isRfresh) {
        addToCompositeDis(HttpUtil
                .speakRecord(getType(), page)
                .subscribe(new Consumer<ResultBean<List<SpeakShare>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<SpeakShare>> bean) throws Exception {
                        refreshUi(bean.getData(), isRfresh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        refreshFailUi(throwable, isRfresh);
                    }
                }));
    }

    protected abstract int getType();
}
