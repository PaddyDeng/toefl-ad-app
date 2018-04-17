package io.dcloud.H58E83894.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.base.adapter.callback.OnLoadMoreListener;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.weiget.pullrefresh.PullRefreshLayout;

public abstract class BaseRefreshFragment<T> extends BaseFragment implements PullRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    protected PullRefreshLayout mPullRefreshLayout;
    protected BaseRecyclerViewAdapter<T> adapter;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_base, container, false);
        ButterKnife.bind(this, view);
        mPullRefreshLayout.setOnRefreshListener(this);
        if (adapter == null) {
            initNewsList();
        }
        adapter.onShowInitView(true);
        if (autoAsyn())
            asyncRequest();
        return view;
    }

    protected boolean autoAsyn() {
        return true;
    }

    public void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type) {
        updateRecycleView(list, msg, type, false);
    }

    public void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustShowNull) {
        mPullRefreshLayout.finishRefresh();

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


    private void initNewsList() {
        adapter = getAdapter();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) mRecyclerView.getAdapter();
                List data = adapter.getAdapterData();
                if (data != null && !data.isEmpty() && position < data.size()) {
                    setListener(data, position);
                }
            }
        });

        if (canLoadMore()) {
            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //加载更多
                    asyncLoadMore();
                }
            });
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(getLayoutManager());
        initRecyclerViewItemDecoration(mRecyclerView);
//        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(adapter);

    }

    protected boolean canLoadMore() {
        return true;
    }

    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {

    }

    /**
     * item listener
     */
    protected void setListener(List<T> data, int position) {
    }

    protected abstract void asyncLoadMore();

    protected abstract BaseRecyclerViewAdapter<T> getAdapter();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    /**
     * 初始化数据
     */
    protected abstract void asyncRequest();

    @Override
    public void refresh() {
        asyncRequest();
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

    protected void refreshFailUi(Throwable throwable, boolean refresh) {
        if (refresh) {
            updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
        }
    }


}
