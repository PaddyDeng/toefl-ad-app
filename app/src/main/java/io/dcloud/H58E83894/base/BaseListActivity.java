package io.dcloud.H58E83894.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.base.adapter.callback.OnLoadMoreListener;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.weiget.pullrefresh.PullRefreshLayout;

/**
 * Created by fire on 2017/7/27  14:56.
 */

public abstract class BaseListActivity<T> extends BaseActivity implements PullRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    protected PullRefreshLayout mPullRefreshLayout;
    @BindView(R.id.base_list_title)
    protected TextView baseTitleTxt;
    @BindView(R.id.base_container)
    protected LinearLayout mBaseContainer;
    @BindView(R.id.title_line)
    protected View mBaseTitleLine;
    protected BaseRecyclerViewAdapter<T> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list_layout);
    }

    @Override
    protected void initData() {
        mPullRefreshLayout.setOnRefreshListener(this);
        if (adapter == null) {
            initNewsList();
        }
        adapter.onShowInitView(true);
        asyncRequest();
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
                if (data != null && !data.isEmpty()) {
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
}
