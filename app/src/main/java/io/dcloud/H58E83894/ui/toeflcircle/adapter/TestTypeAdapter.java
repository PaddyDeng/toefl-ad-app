package io.dcloud.H58E83894.ui.toeflcircle.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.callback.OnAdapterListener;
import io.dcloud.H58E83894.callback.SimpleAdapter;
import io.dcloud.H58E83894.data.TestTypeData;

public class TestTypeAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<TestTypeData> mDatas;
    private OnAdapterListener<TestTypeData> mOnItemClickListener;

    public TestTypeAdapter(List<TestTypeData> datas) {
        mDatas = datas;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_type_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        Context context = holder.itemView.getContext();
        ImageView desTv = holder.getImageView(R.id.test_type_item_tv);
        final TestTypeData data = mDatas.get(position);
        int drawable1 = context.getResources().getIdentifier(data.getDrawableName(), "drawable", context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context, drawable1);
        drawable.setBounds(100, 0, 100, 0);
        desTv.setImageDrawable(drawable);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(v, position, data);
                }
            }
        });
    }

    public void setOnItemClickListener(SimpleAdapter<TestTypeData> itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
