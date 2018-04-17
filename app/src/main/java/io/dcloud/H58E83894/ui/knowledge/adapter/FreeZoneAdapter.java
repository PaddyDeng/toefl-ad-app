package io.dcloud.H58E83894.ui.knowledge.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.circle.PhotoInfo;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.data.know.KnowZoneData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.knowledge.KnowDetailActivity;
import io.dcloud.H58E83894.ui.toeflcircle.ImagePagerActivity;
import io.dcloud.H58E83894.ui.toeflcircle.RemarkNewMsgActivity;
import io.dcloud.H58E83894.ui.toeflcircle.listener.PriaseListener;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.TimeUtils;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.commentview.CommentListView;
import io.dcloud.H58E83894.weiget.commentview.MultiImageView;

public class FreeZoneAdapter extends BaseRecyclerViewAdapter<KnowZoneData.FreeBean> {

    public FreeZoneAdapter(Context context, List<KnowZoneData.FreeBean> data) {
        super(context, data);
    }

    public FreeZoneAdapter(Context context, List<KnowZoneData.FreeBean> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
            return R.layout.item_konw_free_zone;

    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, final KnowZoneData.FreeBean itemData) {
        final Context context = holder.itemView.getContext();
        int type = getEveryItemViewType(position);

            holder.getTextView(R.id.konw_time).setText(context.getString(R.string.str_know_time, itemData.getCreateTime()));
            holder.getTextView(R.id.konw_title).setText(itemData.getName());
            holder.getView(R.id.know_free_linear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemData!= null) {
                        KnowDetailActivity.startKnowDetail(context, itemData.getId());
                    }
                }
            });

    }

}
