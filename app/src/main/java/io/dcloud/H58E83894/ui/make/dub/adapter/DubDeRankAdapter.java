package io.dcloud.H58E83894.ui.make.dub.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.prelesson.adapter.CardHotAdapterHelper;
import io.dcloud.H58E83894.utils.GlideUtil;

public class DubDeRankAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<LessonData> lists;
    private OnItemClickListener mListener;
    private CardHotAdapterHelper mCardAdapterHelper = new CardHotAdapterHelper();

    public DubDeRankAdapter(List<LessonData> lists) {
        this.lists = lists;
    }

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dub_rank, parent, false);
//        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Context context = holder.itemView.getContext();
        LessonData data = lists.get(position);
        //标题
        holder.getTextView(R.id.rank_sum).setText(position+"");//1234
        holder.getTextView(R.id.rank_zan_sum).setText(position+"");//总共多个攒
        holder.getTextView(R.id.rank_name).setText(position+"");//名字
        holder.getTextView(R.id.rank_time).setText(position+"");//时间
        //图片
        GlideUtil.load(RetrofitProvider.TOEFLURL + data.getImage(), holder.getImageView(R.id.rank_head));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.onClick(v, position);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }
}
