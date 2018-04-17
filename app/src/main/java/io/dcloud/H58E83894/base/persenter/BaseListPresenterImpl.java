package io.dcloud.H58E83894.base.persenter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;



import java.util.List;

import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.base.adapter.callback.OnLoadMoreListener;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.base.view.BaseListView;
import io.dcloud.H58E83894.utils.media.SwipeInit;
import io.dcloud.H58E83894.weiget.pullrefresh.PullRefreshLayout;

/**
 * Created by fire on 2017/8/21  10:28.
 */

public class BaseListPresenterImpl<T extends BaseListView, V> implements BaseListPresenter<V>,
        PullRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnLoadMoreListener {

    protected PullRefreshLayout mPullRefreshLayout;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseRecyclerViewAdapter<V> adapter;
    protected T mView;

    public BaseListPresenterImpl(T view) {
        mView = view;
    }

    @Override
    public void view(PullRefreshLayout mPullRefreshLayout, RecyclerView mRecyclerView) {
        this.mPullRefreshLayout = mPullRefreshLayout;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public void view(SwipeRefreshLayout mSwipeRefreshLayout, RecyclerView mRecyclerView) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public void initView() {

        mPullRefreshLayout.setOnRefreshListener(this);

        if (adapter == null) {
            initNewsList();
        }

        adapter.onShowInitView(true);

        if (autoAsyncData()) {
            mView.asyncRequest();
        }
    }

    @Override
    public void initSwipeView() {

        mSwipeRefreshLayout.setOnRefreshListener(this);
        SwipeInit.setRefreshColor(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setRefreshing(true);

        if (adapter == null) {
            initNewsList();
        }

        if (autoAsyncData()) {
            mView.asyncRequest();
        }
    }

    @Override
    public void updateRecycleView(List<V> list, String msg, @InitDataType.InitDataTypeChecker int type) {
        updateRecycleView(list, msg, type, false);//重新加载后,默认不显示空布局
    }

    @Override
    public void updateRecycleView(List<V> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustShowNull) {
        if (mPullRefreshLayout != null)
            mPullRefreshLayout.finishRefresh();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        adapter.onShowInitView(false);
        adapter.onShowEmptyView(false, msg);
        switch (type) {
            case InitDataType.TYPE_REFRESH_SUCCESS:
                if (mustShowNull && (list == null || list.isEmpty())) {
                    adapter.updateNull();
                    adapter.onShowEmptyView(true, msg);
                } else {
                    adapter.update(list);
                }
                break;
            case InitDataType.TYPE_LOAD_MORE_SUCCESS:
//                    adapter.setShowFooter(false);
                adapter.onLoadMoreSuccess();
                adapter.addTail(list);
                break;
            case InitDataType.TYPE_REFRESH_FAIL:
//                if (adapter.getItemCount() == 0) {
//                    //显示空布局
//                    adapter.onShowEmptyView(true, msg);
//                }
                if (adapter.getAdapterData() == null || adapter.getAdapterData().isEmpty()) {
                    adapter.onShowEmptyView(true, msg);
//                    adapter.notifyItemChanged();
                    adapter.notifyDataSetChanged();
                }
                break;
            case InitDataType.TYPE_LOAD_MORE_FAIL:
                adapter.onLoadMoreFail(msg);
                break;
        }
    }

    @Override
    public boolean autoAsyncData() {
        return mView == null ? false : mView.autoAsyncData();
    }

    private void initNewsList() {
        if (mView == null) return;
        adapter = mView.getAdapter();

        adapter.setOnItemClickListener(this);

        if (mView.canAutoLoadMore())
            adapter.setOnLoadMoreListener((OnLoadMoreListener) this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mView.getLayoutManager());
        mView.initRecyclerViewItemDecoration(mRecyclerView);
//        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void refresh() {
        mView.asyncRequestBefore();
        mView.asyncRequest();
    }

    @Override
    public void onRefresh() {
        mView.asyncRequestBefore();
        mSwipeRefreshLayout.setRefreshing(true);
        mView.asyncRequest();
    }

    @Override
    public void onClick(View view, int position) {
        List data = adapter.getAdapterData();
        if (mView != null && data != null && !data.isEmpty() && position < adapter.getAdapterData().size()) {
            mView.setListener(data, position);
        }
    }

    @Override
    public void onLoadMore() {
        //加载更多
        if (mView != null)
            mView.asyncLoadMore();
    }
}
