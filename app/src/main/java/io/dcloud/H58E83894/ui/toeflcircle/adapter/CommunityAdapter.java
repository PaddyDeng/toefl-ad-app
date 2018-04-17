package io.dcloud.H58E83894.ui.toeflcircle.adapter;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.circle.CommunityData;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.ui.prelesson.adapter.CardAdapterHelper;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.weiget.FullyLinearLayoutManager;

/**
 * Created by fire on 2017/7/14  14:40.
 */
public class CommunityAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<CommunityData> mList;
    private OnItemClickListener mListener;
    private Context context;
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    public CommunityAdapter(List<CommunityData> list, Context content) {

        mList = list;
        content = content;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.commuity_item_layout, parent, false);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {


        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Context context = holder.itemView.getContext();
        final CommunityData itemData = mList.get(position);
        if (!TextUtils.isEmpty((CharSequence) itemData.getImage())) {
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


    @Override
    public int getItemCount() {
        return mList.size();
    }


}

