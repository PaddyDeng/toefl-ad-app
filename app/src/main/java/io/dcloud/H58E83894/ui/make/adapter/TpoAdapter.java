package io.dcloud.H58E83894.ui.make.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.ListenTpoData;

/**
 * Created by fire on 2017/7/25  14:42.
 */

public class TpoAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<ListenTpoData> mDataList;
    private OnItemClickListener mItemClickListener;

    public TpoAdapter(List<ListenTpoData> dataList) {
        mDataList = dataList;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.tpo_item_layout, null, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        ListenTpoData tpoData = mDataList.get(position);
        holder.getTextView(R.id.listen_item_tv).setText(String.valueOf(tpoData.getTopNumber()));
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
        return mDataList == null ? 0 : mDataList.size();
    }
}
