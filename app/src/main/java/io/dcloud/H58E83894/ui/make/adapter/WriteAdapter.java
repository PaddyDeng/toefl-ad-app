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
 * Created by fire on 2017/7/26  09:43.
 */

public class WriteAdapter extends BaseRecyclerViewAdapter<PracticeData> {

    private int startNum;
    private Random mRandom;

    public WriteAdapter(Context context, List<PracticeData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        mRandom = new Random();
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
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
        Context context = holder.itemView.getContext();
        int index = startNum + position;
        String serialNum = String.valueOf(index);
        if (index < 10) {
            serialNum = "0" + index;
        }
        holder.getTextView(R.id.write_item_serial_num).setText(serialNum);
        holder.getTextView(R.id.write_item_title).setText(itemData.getCatName());
        int num = mRandom.nextInt(150) + 50 + Integer.valueOf(itemData.getNum());
        holder.getTextView(R.id.write_item_view_count).setText(context.getString(R.string.str_write_independent_practiec, num));
    }
}
