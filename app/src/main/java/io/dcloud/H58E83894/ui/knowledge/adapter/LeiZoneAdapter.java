package io.dcloud.H58E83894.ui.knowledge.adapter;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.nio.charset.IllegalCharsetNameException;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.LeidouReData;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.data.know.KnowZoneData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.knowledge.KnowDetailActivity;
import io.dcloud.H58E83894.ui.knowledge.PricePayLessonActivity;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LeiZoneAdapter extends BaseRecyclerViewAdapter<KnowZoneData.BeanBean> {

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }
    public LeiZoneAdapter(Context context, List<KnowZoneData.BeanBean> data) {
        super(context, data);
    }

    public LeiZoneAdapter(Context context, List<KnowZoneData.BeanBean> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
            return R.layout.item_konw_lei_zone;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, final KnowZoneData.BeanBean itemData) {

        final Context context = holder.itemView.getContext();
            holder.getTextView(R.id.konw_title).setText(itemData.getName());
            holder.getTextView(R.id.konw_time).setText(context.getString(R.string.str_know_time, itemData.getCreateTime()));
            GlideUtil.load(RetrofitProvider.TOEFLURL + itemData.getImage(), holder.getImageView(R.id.know_detail_iv));//图片
            holder.getTextView(R.id.konw_titles).setText(itemData.getDescription());
            if(itemData.getIsBuy() == 0){
                holder.getTextView(R.id.know_money).setText(context.getString(R.string.str_leidou_price, itemData.getPrice()));
                holder.getTextView(R.id.know_money).setOnClickListener(new  View.OnClickListener(){

                    @Override
                    public void onClick(View v) {

                        PricePayLessonActivity.startPre(context, itemData.getPrice(), itemData.getId());



                    }
                });
                holder.getView(R.id.linear_know_lei).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.toastShort(context, "若是想要查看，请先进行购买");
                    }
                });
            }else {
                holder.getTextView(R.id.know_money).setBackground(context.getResources().getDrawable(R.drawable.about_know_orange_bg));
                holder.getTextView(R.id.know_money).setText("已购买");
                holder.getView(R.id.linear_know_lei).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KnowDetailActivity.startKnowDetail(context, itemData.getId());
                    }
                });

            }
           //雷豆价格

//            holder.getView(R.id.konw_title).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    KnowDetailActivity.startKnowDetail(context, itemData.getId());
//                }
//            });
        }
    }


