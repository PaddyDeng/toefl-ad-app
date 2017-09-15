package io.dcloud.H58E83894.ui.center.record.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.record.ReadContent;
import io.dcloud.H58E83894.data.record.ReadRecordData;

/**
 * Created by fire on 2017/8/7  09:30.
 */

public class ReadRecordAdapter extends BaseRecyclerViewAdapter<ReadRecordData> {

    public ReadRecordAdapter(Context context, List<ReadRecordData> data, RecyclerView.LayoutManager mLayoutManager) {
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
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, ReadRecordData itemData) {
        List<ReadContent> content = itemData.getContent();
        if (content == null || content.isEmpty()) return;
        ReadContent readContent = content.get(0);
        String foot = itemData.getTimeFoot();
        holder.getTextView(R.id.record_item_time_tv).setText(foot.substring(0, foot.lastIndexOf(":")));
        String time = itemData.getCreateTime();
        String[] split = time.split(" ");
        String date = split[0].replace("-", "/");
        holder.getTextView(R.id.record_item_date_tv).setText(date);
        String title = readContent.getCatName() + "-" + readContent.getTitle();
        holder.getTextView(R.id.record_item_title_tv).setText(title);
        int record = itemData.getUserAnswerRecord();
        int son = itemData.getSon();
        String endHint = mContext.getString(R.string.str_end, record, son);
        holder.getTextView(R.id.record_item_end_tv).setText(endHint);

        TextView textView = holder.getTextView(R.id.record_status_tv);
        if (record == son) {
            textView.setText(mContext.getString(R.string.str_see_result));
        } else {
            textView.setText(mContext.getString(R.string.str_go_on_practice));
        }

    }
}
