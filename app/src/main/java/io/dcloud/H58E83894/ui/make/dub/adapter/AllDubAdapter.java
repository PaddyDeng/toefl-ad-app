package io.dcloud.H58E83894.ui.make.dub.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.utils.GlideUtil;

public class AllDubAdapter extends BaseRecyclerViewAdapter<LessonData> {


    public AllDubAdapter(Context context, List<LessonData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.dub_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, LessonData itemData) {

        Context context = holder.itemView.getContext();
//        LessonData data = lists.get(position);
        //标题
        holder.getTextView(R.id.hot_item_des).setText(itemData.getName());
        //图片
        GlideUtil.load(RetrofitProvider.TOEFLURL + itemData.getImage(), holder.getImageView(R.id.hot_item_bg));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.onClick(v, position);
//                }
//            }
//        });

    }
}