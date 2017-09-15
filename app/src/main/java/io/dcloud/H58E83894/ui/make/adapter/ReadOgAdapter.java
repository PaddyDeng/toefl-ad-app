package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.make.PracticeData;

/**
 * Created by fire on 2017/8/3  09:28.
 */

public class ReadOgAdapter extends BaseRecyclerViewAdapter<PracticeData> {

    private Random mRandom;

    public ReadOgAdapter(Context context, List<PracticeData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        mRandom = new Random();
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.write_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, PracticeData itemData) {
        int index = position + 1;
        if (index < 10) {
            holder.getTextView(R.id.write_item_serial_num).setText("0" + index);
        } else {
            holder.getTextView(R.id.write_item_serial_num).setText(String.valueOf(index));
        }
        holder.getTextView(R.id.write_item_title).setText(itemData.getName());
        int num = mRandom.nextInt(150) + 50 + Integer.valueOf(itemData.getNum());
        holder.getTextView(R.id.write_item_view_count).setText(mContext.getString(R.string.str_write_independent_practiec, num));
    }
}
