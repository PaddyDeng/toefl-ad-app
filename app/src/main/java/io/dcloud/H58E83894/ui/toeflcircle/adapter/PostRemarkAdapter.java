package io.dcloud.H58E83894.ui.toeflcircle.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caimuhao.rxpicker.bean.ImageItem;

import java.io.File;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.utils.Utils;

public class PostRemarkAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<ImageItem> mDatas;
    private OnItemClickListener choosePicListener;
    private OnItemClickListener itemClickListener;
    private boolean needAddNullImageItem;

    public PostRemarkAdapter(List<ImageItem> mDatas) {
        this.mDatas = mDatas;
    }

    public void setDatas(List<ImageItem> lists, boolean needAddNullImageItem) {
        if (lists == null || lists.isEmpty()) return;
        mDatas = lists;
        this.needAddNullImageItem = needAddNullImageItem;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.post_remark_img_item_layout, parent, false));
    }

    public void setChoosePicListener(OnItemClickListener choosePicListener) {
        this.choosePicListener = choosePicListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ImageItem item = mDatas.get(position);
        ImageView iv = holder.getImageView(R.id.post_dele_choose_iv);
        ImageView view = holder.getImageView(R.id.post_remark_iv);
        if (!TextUtils.isEmpty(item.getPath())) {
            Utils.setVisible(iv);
            File file = new File(item.getPath());
            Glide.with(holder.itemView.getContext()).load(file).asBitmap().into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(v, position);
                    }
                }
            });
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(item);
                    if (needAddNullImageItem) {
                        mDatas.add(new ImageItem());
                        needAddNullImageItem = false;
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            Utils.setGone(iv);
            view.setImageResource(R.drawable.choose_pic);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.logh("==", "click");
                    if (choosePicListener != null && position == (mDatas.size() - 1)) {
                        choosePicListener.onClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
