package io.dcloud.H58E83894.ui.center.glossary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.GlossaryWordData;
import io.dcloud.H58E83894.data.TranslateData;

/**
 * Created by fire on 2017/8/11  16:12.
 */

public class GlossaryWordAdapter extends BaseRecyclerViewAdapter<GlossaryWordData> {

    public GlossaryWordAdapter(Context context, List<GlossaryWordData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.glossary_word_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, GlossaryWordData itemData) {
        TranslateData translate = itemData.getTranslate();
        holder.getTextView(R.id.item_word_tv).setText(itemData.getWord());
        holder.getTextView(R.id.item_word_phonetic_tv).setText(mContext.getString(R.string.str_word_phonetic, translate.getUk(), translate.getUs()));
        StringBuffer sb = new StringBuffer();
        List<String> explains = translate.getExplains();
        for (int i = 0, size = explains.size(); i < size; i++) {
            sb.append(explains.get(i));
            if (i < size - 1) {
                sb.append("\n");
            }
        }
        holder.getTextView(R.id.item_word_mean_tv).setText(sb.toString());
    }
}
