package io.dcloud.H58E83894.ui.prelesson.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.prelesson.PreProGramLessonActivity;
import io.dcloud.H58E83894.utils.GlideUtil;

public class CardAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<LessonData> mList;
    private CardsAdapterHelper mCardAdapterHelper = new CardsAdapterHelper();
    private OnItemClickListener mListener;

    public CardAdapter(List<LessonData> mList) {
        this.mList = mList;
    }

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());

        final LessonData data = mList.get(position);
//        String headUrl = RetrofitProvider.SMARTAPPLYURL + data.getImage();
        String headUrl = "http://open.viplgw.cn/" + data.getImage();
//        GlideUtil.load(headUrl, (ImageView) holder.getView(R.id.public_lesson_head));
        GlideUtil.load(headUrl, (ImageView) holder.getView(R.id.gmat_public_lesson_img));
        holder.getTextView(R.id.public_lesson_title).setText(data.getName());
        holder.getTextView(R.id.public_lesson_name).setText(data.getListeningFile());

        holder.getTextView(R.id.public_lesson_num).setText(holder.itemView.getContext().getResources().getString(R.string.str_pub_lesson_num, data.getViewCount()));

        holder.getTextView(R.id.immediately_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null && !TextUtils.isEmpty(data.getId())) {
                    PreProGramLessonActivity.startPre(holder.itemView.getContext(), data.getId(), data.getName());
                }
            }
        });

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
        return mList.size();
    }


}
