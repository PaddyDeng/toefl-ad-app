package io.dcloud.H58E83894.ui.prelesson.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.utils.GlideUtil;

public class LessonAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<LessonData> mList;
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private OnItemClickListener mListener;

    public LessonAdapter(List<LessonData> list) {
        mList = list;
    }

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamt_lesson_item_layout, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Context context = holder.itemView.getContext();
        LessonData data = mList.get(position);
        GlideUtil.load(RetrofitProvider.TOEFLURL + data.getImage(), holder.getImageView(R.id.lesson_item_img));
        holder.getTextView(R.id.lesson_item_title).setText(data.getName());
        if (TextUtils.isEmpty(data.getNumbering())) {
            holder.getTextView(R.id.lesson_item_start_time).setText(context.getString(R.string.str_lesson_every_open));
        } else {
            holder.getTextView(R.id.lesson_item_start_time).setText(context.getString(R.string.str_lesson_open_time, data.getNumbering()));
        }
        if (!TextUtils.isEmpty(data.getDuration())) {
            holder.getTextView(R.id.lesson_item_lesson_time).setText(context.getString(R.string.str_lesson_course_time, data.getDuration()));
        }
        holder.getTextView(R.id.lesson_item_price).setText(context.getString(R.string.str_lesson_price, data.getPrice()));
        holder.getTextView(R.id.lesson_item_search_num).setText(context.getString(R.string.str_lesson_search_num, data.getViewCount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}