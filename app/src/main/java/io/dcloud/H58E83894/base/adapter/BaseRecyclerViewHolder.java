package io.dcloud.H58E83894.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.weiget.CircleImageView;
import io.dcloud.H58E83894.weiget.commentview.CommentListView;
import io.dcloud.H58E83894.weiget.commentview.MultiImageView;

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //集合类，layout里包含的View,以view的id作为key，value是view对象
    protected SparseArray<View> mViews;
    //上下文对象
    public Context mContext;
    private OnItemClickListener onItemClickListener;

    public BaseRecyclerViewHolder(Context mContext, View itemView) {
        super(itemView);
        this.mContext = mContext;
        mViews = new SparseArray<>();
        itemView.setOnClickListener(this);
    }

    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ImageView getImageView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return findViewById(viewId);
    }


    public CircleImageView getCircleImageView(int viewId) {
        return findViewById(viewId);
    }



    public CommentListView getCommLV(int viewId) {
        return findViewById(viewId);
    }

    public MultiImageView getMultiImageView(int viewId) {
        return findViewById(viewId);
    }

    public <V extends View> V getView(int viewId) {
        return (V) findViewById(viewId);
    }

    /****
     * 以下为辅助方法
     *****/

    public BaseRecyclerViewHolder setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        tv.setText(text);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (null != onItemClickListener) {
            onItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
