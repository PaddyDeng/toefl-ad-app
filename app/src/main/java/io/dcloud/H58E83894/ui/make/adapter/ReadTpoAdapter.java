package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;
import java.util.Random;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.Utils;

/**
 * Created by fire on 2017/8/2  15:34.
 */

public class ReadTpoAdapter extends BaseRecyclerViewAdapter<PracticeData> {

    private int itemHeight;
    private Random mRandom;

    public ReadTpoAdapter(Context context, List<PracticeData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        mRandom = new Random();
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    @Override
    protected void onHolderItemHeight(View view) {
        view.getLayoutParams().height = itemHeight;
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.read_tpo_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, PracticeData itemData) {
        if (position == 0) {
            Utils.setVisible(holder.getView(R.id.place_line));
        }
        ImageView imageView = holder.getImageView(R.id.read_tpo_item_iv);
        holder.getTextView(R.id.read_tpo_item_title_des).setText(mContext.getString(R.string.str_passage, position + 1));
        holder.getTextView(R.id.read_item_tpo_des).setText(mContext.getString(R.string.str_write_independent_practiec, 50 + mRandom.nextInt(100)));
        String url = RetrofitProvider.TOEFLURL + itemData.getImage();
        if (url.endsWith(".gif")) {
            GlideUtil.loadAsGif(url, imageView);
        } else {
            GlideUtil.load(url, imageView);
        }
    }
}
