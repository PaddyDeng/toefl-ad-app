package io.dcloud.H58E83894.ui.center.lesson;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;

/**
 * Created by fire on 2017/8/16  13:42.
 */

public class LessonCenterAdapter extends BaseRecyclerViewAdapter<Object> {

    public LessonCenterAdapter(Context context, List<Object> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return 0;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, Object itemData) {

    }
}
