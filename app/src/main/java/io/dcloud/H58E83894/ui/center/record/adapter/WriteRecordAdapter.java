package io.dcloud.H58E83894.ui.center.record.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.record.WriteRecordData;

/**
 * Created by fire on 2017/8/7  13:46.
 */

public class WriteRecordAdapter extends BaseRecyclerViewAdapter<WriteRecordData> {

    public WriteRecordAdapter(Context context, List<WriteRecordData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.record_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, WriteRecordData itemData) {
        String time = itemData.getCreateTime();
        String[] split = time.split(" ");
        String date = split[0].replace("-", "/");
        holder.getTextView(R.id.record_item_date_tv).setText(date);
        String foot = split[1];
        holder.getTextView(R.id.record_item_time_tv).setText(foot.substring(0, foot.lastIndexOf(":")));
        holder.getTextView(R.id.record_item_title_tv).setText(itemData.getCatName() + "-" + itemData.getNum());
        String des = itemData.getCatName().contains("TPO") ? "Integrated Writing Task" : "Independent Writing Task";
        holder.getTextView(R.id.record_item_end_tv).setText(des);
        holder.getTextView(R.id.record_status_tv).setText(mContext.getString(R.string.str_see_result));
    }
}
