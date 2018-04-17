package io.dcloud.H58E83894.ui.make.everyDayListen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.MyLessonData;
import io.dcloud.H58E83894.data.instestlisten.AllInstListenData;
import io.dcloud.H58E83894.data.record.ReadContent;
import io.dcloud.H58E83894.data.record.ReadRecordData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.utils.GlideUtil;

import static io.dcloud.H58E83894.ui.make.everyDayListen.EveryListenPracticeActivity.startEveryListenPractActivity;

/**
 * Created by fire on 2017/8/16  13:42.
 */

public class MyAllEveListenAdapter extends BaseRecyclerViewAdapter<AllInstListenData.DataBean> {


    private Context context;
    private MyLessonData myLessonData;
    private int currentTimes =0;
    private  int ExpireTime =0;

    public MyAllEveListenAdapter(Context context, List<AllInstListenData.DataBean> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        this.context = context;
    }

    @Override
    public int bindItemViewLayout(int viewType) {

       return R.layout.item_eve_my_all;
//        return R.popup_window_ranking_classify.item_center_my_lesson;
    }

    @Override
    public int getEveryItemViewType(int position) {

//        if (currentTimes < ExpireTime) {
//
//        }
            return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, final AllInstListenData.DataBean itemData) {

        if(TextUtils.isEmpty(itemData.toString()) || itemData.toString().length() == 0 || itemData.toString().equals("nuill")){ return;}
        String times = (itemData.getCreateTime()).substring(0,10);
        holder.getTextView(R.id.eve_time).setText(times);
        GlideUtil.load(RetrofitProvider.TOEFLURL + itemData.getImage(), holder.getImageView(R.id.ever_iv));
        holder.getTextView(R.id.eve_title).setText(itemData.getTitle());

        holder.getView(R.id.linear_lesson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEveryListenPractActivity(context, itemData.getId());
            }
        });

        }



}

