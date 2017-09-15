package io.dcloud.H58E83894.ui.toeflcircle;

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
import io.dcloud.H58E83894.data.circle.CommunityData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.toeflcircle.adapter.CommunityAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/14  11:26.
 */

public class CommunityFragment extends BaseRefreshFragment<CommunityData> {


    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
    private int page;
    private Observable<Boolean> refreshObs;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshObs = RxBus.get().register(C.TOEFL_CIRCLE_POST_COMMUNITY, Boolean.class);
        refreshObs.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                asyncRequest();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshObs != null)
            RxBus.get().unregister(C.TOEFL_CIRCLE_POST_COMMUNITY, refreshObs);
    }

    @Override
    protected void asyncLoadMore() {
        ++page;
        asyncData(false);
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        asyncData(true);
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
    }

    private void asyncData(final boolean refresh) {
        addToCompositeDis(HttpUtil.getPostList(String.valueOf(page))
                .subscribe(new Consumer<ResultBean<List<CommunityData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<CommunityData>> bean) throws Exception {
                        List<CommunityData> data = bean.getData();
                        if (data != null && !data.isEmpty()) {
                            if (refresh) {
                                updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            asyncFail(refresh);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        asyncFail(refresh);
                    }
                }));
    }

    @Override
    protected void setListener(List<CommunityData> data, int position) {
        super.setListener(data, position);
        CommunityData data1 = data.get(position);
        CommunityDetailActivity.startCommunity(getActivity(), data1.getId());
    }

    private void asyncFail(boolean refresh) {
        if (refresh) {
            updateRecycleView(null, getString(R.string.str_no_data_tip), InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, getString(R.string.str_no_data_tip), InitDataType.TYPE_LOAD_MORE_FAIL);
        }
    }

    @Override
    protected BaseRecyclerViewAdapter<CommunityData> getAdapter() {
        return new CommunityAdapter(getActivity(), null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

}
