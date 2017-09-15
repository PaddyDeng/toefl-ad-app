package io.dcloud.H58E83894.ui.center.glossary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.GlossaryData;
import io.dcloud.H58E83894.utils.TimeUtils;

/**
 * Created by fire on 2017/8/11  15:17.
 */

public class GlossaryAdapter extends BaseRecyclerViewAdapter<GlossaryData> {

    public GlossaryAdapter(Context context, List<GlossaryData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.glossary_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, GlossaryData itemData) {
        holder.getTextView(R.id.glossary_item_topic_num_tv).setText(mContext.getString(R.string.str_glossary_word_num, itemData.getNum()));
        String startTime = TimeUtils.longToString(Long.parseLong(itemData.getStartTime()) * 1000, "yyyy/MM/dd");
        String endTime = TimeUtils.longToString(Long.parseLong(itemData.getEndTime()) * 1000, "yyyy/MM/dd");
        holder.getTextView(R.id.item_glossary_tv).setText(mContext.getString(R.string.str_glossary_time_des, startTime, endTime));
    }
}
