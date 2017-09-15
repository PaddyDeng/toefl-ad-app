package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.core.CoreAnswerBean;
import io.dcloud.H58E83894.utils.C;

/**
 * Created by fire on 2017/7/19  10:42.
 */

public class CoreAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private int itemHeight;
    private List<CoreAnswerBean> mList;

    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public CoreAdapter(List<CoreAnswerBean> list) {
        mList = list;
    }

    public void update(List<CoreAnswerBean> list) {
        if (list == null || list.isEmpty()) return;
        mList = list;
        notifyDataSetChanged();
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight / getItemCount();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.core_item_layout, parent, false);
        view.getLayoutParams().height = itemHeight;
        return new BaseRecyclerViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final CoreAnswerBean bean = mList.get(position);
        Context context = holder.itemView.getContext();
        TextView textView = holder.getTextView(R.id.core_option_content);
        textView.setText(bean.getContent());
        if (bean.getStatus() == C.CORRECT) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.color_sup_green));
        } else if (bean.getStatus() == C.ERROR) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.color_sup_red));
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.color_txt_dark_gray));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
