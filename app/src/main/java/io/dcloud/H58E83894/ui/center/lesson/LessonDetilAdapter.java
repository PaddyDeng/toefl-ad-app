package io.dcloud.H58E83894.ui.center.lesson;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.MyLessonData;

/**
 * Created by fire on 2017/8/16  13:42.
 */

public class LessonDetilAdapter extends BaseRecyclerViewAdapter<MyLessonData.DataBean.DatailsBean> {

    public LessonDetilAdapter(Context context, List<MyLessonData.DataBean.DatailsBean> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    private OnItemClickListener mListener;

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.item_lesson_detail;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }


    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, final MyLessonData.DataBean.DatailsBean itemData) {

        final Context context = holder.itemView.getContext();

        holder.getTextView(R.id.konw_title).setText(itemData.getName());
//        holder.getView(R.id.know_free_linear).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                MaEfActivity.startMaEfActivity(context, itemData);
//
////                DealActivity.startDealActivity(context,"jjj","http://order.gmatonline.cn/pay/video/video?id=444&videoId=85");
//
//            }
//        });


    }
}
