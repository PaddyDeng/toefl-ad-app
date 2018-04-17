package io.dcloud.H58E83894.ui.center.know;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.LeidouReData;
import io.dcloud.H58E83894.data.ListsData;
import io.dcloud.H58E83894.data.know.KnowZoneData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.knowledge.KnowDetailActivity;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyLeiZoneAdapter extends BaseRecyclerViewAdapter<ListsData.ListBean>  {

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }
    public MyLeiZoneAdapter(Context context, List<ListsData.ListBean>  data) {
        super(context, data);
    }

    public MyLeiZoneAdapter(Context context, List<ListsData.ListBean>  data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
            return R.layout.item_center_my_know;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }


    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, final int position, final ListsData.ListBean itemData) {

        final Context context = holder.itemView.getContext();
            holder.getTextView(R.id.konw_title).setText(itemData.getName().trim());
            holder.getTextView(R.id.konw_time).setText(context.getString(R.string.str_know_time, itemData.getCreateTime()));
            GlideUtil.load(RetrofitProvider.TOEFLURL + itemData.getImage(), holder.getImageView(R.id.know_detail_iv));//图片
            holder.getTextView(R.id.konw_titles).setText(itemData.getDescription());
            holder.getView(R.id.linear_know).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KnowDetailActivity.startKnowDetail(context, itemData.getId());
                }
            });
        }
    }


