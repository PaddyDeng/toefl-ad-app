package io.dcloud.H58E83894.ui.toeflcircle.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.circle.CommunityData;
import io.dcloud.H58E83894.utils.GlideUtil;

/**
 * Created by fire on 2017/7/14  14:40.
 */
public class CommunityAdapter extends BaseRecyclerViewAdapter<CommunityData> {

    public CommunityAdapter(Context context, List<CommunityData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        if (viewType == 100) {
            return R.layout.community_one_item_layout;
        }
        return R.layout.commuity_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        if (position == 0) {
            return 100;
        }
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, CommunityData itemData) {
        Context context = holder.itemView.getContext();
        int type = getEveryItemViewType(position);

        if (type == 100) {
            //第一条
            if (!TextUtils.isEmpty(itemData.getImage())) {
                GlideUtil.loadDefault(itemData.getImage(), holder.getCircleImageView(R.id.community_head_iv), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
            }
            String name = itemData.getUsername();
            if (!TextUtils.isEmpty(itemData.getNickname())) {
                name = itemData.getNickname();
            }
            holder.getTextView(R.id.community_name_tv).setText(name);
            holder.getTextView(R.id.community_one_title_tv).setText(itemData.getTitle());
            holder.getTextView(R.id.community_one_post_time).setText(context.getString(R.string.str_community_post, itemData.getDateTime()));
            holder.getTextView(R.id.community_one_right_tv).setText(itemData.getViewCount());
        } else {
            if (!TextUtils.isEmpty(itemData.getImage())) {
                GlideUtil.loadDefault(itemData.getImage(), holder.getCircleImageView(R.id.community_item_head), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
            }
            String name = itemData.getUsername();
            if (!TextUtils.isEmpty(itemData.getNickname())) {
                name = itemData.getNickname();
            }
            holder.getTextView(R.id.community_item_name).setText(name);
            holder.getTextView(R.id.community_item_title).setText(itemData.getTitle());
            holder.getTextView(R.id.community_item_post_time).setText(context.getString(R.string.str_community_post, itemData.getDateTime()));
            holder.getTextView(R.id.community_item_right_view).setText(itemData.getViewCount());
        }
    }
}

