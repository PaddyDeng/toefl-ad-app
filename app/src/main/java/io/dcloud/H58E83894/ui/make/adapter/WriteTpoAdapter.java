package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.data.make.type.TpoDes;
import io.dcloud.H58E83894.data.make.type.TpoTitle;
import io.dcloud.H58E83894.data.make.type.TypeTpo;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.Utils;

/**
 * Created by fire on 2017/7/27  09:26.
 */

public class WriteTpoAdapter extends BaseRecyclerViewAdapter<TypeTpo> {

    public WriteTpoAdapter(Context context, List<TypeTpo> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        if (viewType == 200) {
            return R.layout.write_tpo_title_item_layout;
        } else if (viewType == 100) {
            return R.layout.write_des_item_layout;
        }
        return 0;
    }

    @Override
    public int getEveryItemViewType(int position) {
        int type = mData.get(position).getType();
        if (type == C.TYPE_TITLE) {//表示title
            return 200;
        } else if (type == C.TYPE_DES) {//描述详情item
            return 100;
        }
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, TypeTpo itemData) {
        int viewType = getEveryItemViewType(position);
        if (viewType == 100) {
            TpoDes des = (TpoDes) itemData;
            PracticeData question = des.getQuestion();
            holder.getTextView(R.id.write_tpo_item_title).setText(question.getName());
            holder.getTextView(R.id.write_tpo_item_view_count)
                    .setText(holder.itemView.getContext().getString(R.string.str_write_independent_practiec, Integer.parseInt(question.getNum())));
            if (des.isLastDesItem()) {
                Utils.setGone(holder.getView(R.id.tpo_write_item_line));
            } else {
                Utils.setVisible(holder.getView(R.id.tpo_write_item_line));
            }
        } else if (viewType == 200) {
            TpoTitle title = (TpoTitle) itemData;
            holder.getTextView(R.id.tpo_title_item_tv).setText(title.getCatName());
        }
    }
}
