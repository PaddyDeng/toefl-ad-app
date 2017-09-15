package io.dcloud.H58E83894.ui.center.record.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.FineListenRecordData;

/**
 * Created by fire on 2017/8/15  09:43.
 */

public class FineListenRecordAdapter extends BaseRecyclerViewAdapter<FineListenRecordData> {

    public FineListenRecordAdapter(Context context, List<FineListenRecordData> data, RecyclerView.LayoutManager mLayoutManager) {
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
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, FineListenRecordData itemData) {
        String time = itemData.getStartTime();
        String[] split = time.split(" ");
        String date = split[0].replace("-", "/");
        holder.getTextView(R.id.record_item_date_tv).setText(date);
        String foot = split[1];
        holder.getTextView(R.id.record_item_time_tv).setText(foot.substring(0, foot.lastIndexOf(":")));

        holder.getTextView(R.id.record_item_title_tv).setText(itemData.getCatName() + "-" + itemData.getTitle() + "-" + itemData.getName());
        holder.getTextView(R.id.record_item_end_tv).setText(mContext.getString(R.string.str_listen_num_hint,itemData.getNum()));

        holder.getTextView(R.id.record_status_tv).setText(mContext.getString(R.string.str_go_on_fine_listen));
    }
}
