package io.dcloud.H58E83894.ui.center.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.MsgData;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.TimeUtils;

public class MsgAdapter extends BaseRecyclerViewAdapter<MsgData> {

    public MsgAdapter(Context context, List<MsgData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.msg_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }


    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, MsgData itemData) {
        long aLong = Long.parseLong(itemData.getCreateTime());
        String time = TimeUtils.longToString(aLong * 1000, "MM月dd日 HH:mm:ss");
        String[] timeList = time.split(" ");
        holder.getTextView(R.id.msg_date_tv).setText(timeList[0]);
        holder.getTextView(R.id.msg_time_tv).setText(timeList[1]);
        String news = itemData.getNews();
        TextView textView = holder.getTextView(R.id.msg_content_tv);
        String type = itemData.getType();
        if (TextUtils.equals(type, "1")) {
            textView.setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_unread_msg, news)));
        } else if (TextUtils.equals(type, "2")) {
            textView.setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_has_read_msg, news)));
        }
    }

}
