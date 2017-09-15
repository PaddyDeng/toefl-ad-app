package io.dcloud.H58E83894.ui.toeflcircle.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.circle.ReplyData;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.TimeUtils;

public class CommDetailAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<ReplyData> mReplyDataList;

    public CommDetailAdapter(List<ReplyData> replyDataList) {
        mReplyDataList = replyDataList;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.comm_detail_re_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        ReplyData data = mReplyDataList.get(position);
        if (!TextUtils.isEmpty(data.getImage())) {
            GlideUtil.loadDefault(data.getImage(), holder.getCircleImageView(R.id.re_item_head_img), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        }
        String name = data.getUsername();
        if (!TextUtils.isEmpty(data.getNickname())) {
            name = data.getNickname();
        }
        holder.getTextView(R.id.remark_new_detail_item_user_name).setText(name);
        holder.getTextView(R.id.remark_new_detail_item_time).setText(TimeUtils.longToString(Long.parseLong(data.getCreateTime()) * 1000, "yyyy-MM-dd"));
        holder.getTextView(R.id.remark_new_detail_re_item_content).setText(data.getContent());

    }

    @Override
    public int getItemCount() {
        return mReplyDataList == null ? 0 : mReplyDataList.size();
    }
}
