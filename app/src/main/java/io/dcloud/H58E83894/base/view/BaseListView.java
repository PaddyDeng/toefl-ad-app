package io.dcloud.H58E83894.base.view;

import android.support.v7.widget.RecyclerView;



import java.util.List;

import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;

/**
 * Created by fire on 2017/8/21  10:27.
 */

public interface BaseListView<T> {

    BaseRecyclerViewAdapter<T> getAdapter();

    RecyclerView.LayoutManager getLayoutManager();

    /**
     * 初始化数据
     */
    void asyncLoadMore();

    void asyncRequest();

    void initRecyclerViewItemDecoration(RecyclerView mRecyclerView);

    /**
     * item listener
     */
    void setListener(List<T> data, int position);

    boolean autoAsyncData();

    /**
     * 可以自动加载更多数据
     */
    boolean canAutoLoadMore();

    void asyncRequestBefore();
}
