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

public class ToeflLessonAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<LessonData> mList;
//    private CardHotAdapterHelper mCardAdapterHelper = new CardHotAdapterHelper();
        private OnItemClickListener mListener;

    public ToeflLessonAdapter(List<LessonData> list) {
        mList = list;
    }

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toefl_item_layout, parent, false);
//        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
//        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Context context = holder.itemView.getContext();
        LessonData data = mList.get(position);

        holder.getTextView(R.id.download_item_title_tv).setText(data.getName());

        if (!TextUtils.isEmpty(data.getDuration())) {
            holder.getTextView(R.id.up_time_tv).setText(context.getString(R.string.str_lesson_course_time, data.getDuration()));
        }
        if (TextUtils.isEmpty(data.getNumbering())) {
            holder.getTextView(R.id.post_up_person_tv).setText(context.getString(R.string.str_lesson_every_open));
        } else {
            holder.getTextView(R.id.post_up_person_tv).setText(context.getString(R.string.str_lesson_open_time, data.getNumbering()));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}