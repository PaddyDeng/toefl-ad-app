package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.make.ListenWriteGridData;

/**
 * Created by fire on 2017/8/11  10:16.
 */

public class ListenWriteAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<ListenWriteGridData> mDataList;

    public ListenWriteAdapter(List<ListenWriteGridData> dataList) {
        mDataList = dataList;
    }

    public void update(List<ListenWriteGridData> datas) {
        if (datas == null || datas.isEmpty()) return;
        mDataList.clear();
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listen_write_item_layout, null, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        final ListenWriteGridData data = mDataList.get(position);
        final Context context = holder.itemView.getContext();
        final EditText et = holder.getView(R.id.write_item_et);
        et.setText(data.getWriteAnswer());
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.equals(data.getCorrectAnswer().toUpperCase(), s.toString().toUpperCase())) {
                    et.setTextColor(ContextCompat.getColor(context, R.color.color_sup_green));
                } else {
                    et.setTextColor(ContextCompat.getColor(context, R.color.color_sup_red));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
}
