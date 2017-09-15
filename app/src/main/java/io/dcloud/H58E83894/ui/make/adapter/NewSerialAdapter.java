package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.OptionInfoData;

/**
 * Created by fire on 2017/8/9  15:35.
 */

public class NewSerialAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<OptionInfoData> lists;
    private Context mContext;
    private int itemWidth;

    public NewSerialAdapter(Context c, List<OptionInfoData> lists) {
        this.lists = lists;
        mContext = c;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_serial_item_layout, parent, false);
        view.getLayoutParams().width = itemWidth;
        return new BaseRecyclerViewHolder(parent.getContext(), view);
    }

    private OnItemClickListener onItemClickListener;

    public void setListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final OptionInfoData data = lists.get(position);
        final TextView serialTv = holder.getTextView(R.id.new_serial_item_tv);
        final View view = holder.getView(R.id.new_serial_item_container);
        serialTv.setText(String.valueOf(data.getTopicNum()));
        noSelectedStatus(data, serialTv, view);
        if (data.isSelected()) {
            serialTv.setTextColor(mContext.getResources().getColor(R.color.color_white));
            if (data.isCorrect()) {
                view.setBackgroundResource(R.drawable.single_sup_circle_green_shape);
            } else {
                view.setBackgroundResource(R.drawable.single_sup_circle_red_shape);
            }
        } else {
            noSelectedStatus(data, serialTv, view);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onClick(v, position);
            }
        });
    }

    private void noSelectedStatus(OptionInfoData data, TextView serialTv, View view) {
        if (data.isCorrect()) {
            serialTv.setTextColor(mContext.getResources().getColor(R.color.color_green));
            view.setBackgroundResource(R.drawable.serial_green_ring);
        } else {
            serialTv.setTextColor(mContext.getResources().getColor(R.color.color_red));
            view.setBackgroundResource(R.drawable.serial_red_ring);
        }
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }
}

