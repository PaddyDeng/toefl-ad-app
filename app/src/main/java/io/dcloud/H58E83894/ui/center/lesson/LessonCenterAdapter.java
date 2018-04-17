package io.dcloud.H58E83894.ui.center.lesson;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.MyLessonData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.HtmlUtil;

import static io.dcloud.H58E83894.weiget.DateUtils.timeslashData;

/**
 * Created by fire on 2017/8/16  13:42.
 */

public class LessonCenterAdapter extends BaseRecyclerViewAdapter<MyLessonData.DataBean> {


    private Context context;
    private MyLessonData myLessonData;
    private int currentTimes =0;
    private  int ExpireTime =0;

    public LessonCenterAdapter(Context context, List<MyLessonData.DataBean> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        this.context = context;
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.item_center_my_lesson;
    }

    @Override
    public int getEveryItemViewType(int position) {

//        if (currentTimes < ExpireTime) {
//
//        }
            return getAdapterData().size();
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, final MyLessonData.DataBean itemData) {


//
        if(!itemData.toString().equals("null") && !TextUtils.isEmpty(itemData.toString())){
            final Context context = holder.itemView.getContext();
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            long currentTime = System.currentTimeMillis() / 1000;
            currentTimes = Integer.parseInt(String.valueOf(currentTime).trim());
            ExpireTime = Integer.parseInt(itemData.getExpireTime().trim());
        if (currentTimes <= ExpireTime) {
            param.height = RelativeLayout.LayoutParams.WRAP_CONTENT;// 这里注意使用自己布局的根布局类型
            param.width = RelativeLayout.LayoutParams.MATCH_PARENT;// 这里注意使用自己布局的根布局类型
            holder.getTextView(R.id.lesson_pay).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_lesson_end_date, timeslashData(itemData.getExpireTime()))));
            holder.getView(R.id.linear_my_lesson_detail).setOnClickListener(new View.OnClickListener() {
                MyLessonData.DataBean.DatailsBean datailsBean;

                @Override
                public void onClick(View v) {
                    MyLessonDetaiActivity.startMyLessonDetai(context, itemData, itemData.getContentName());

                }
            });

            GlideUtil.load(RetrofitProvider.TOEFLURL + itemData.getImage(), holder.getImageView(R.id.lesson_iv));
            holder.getTextView(R.id.konw_titles).setText(itemData.getContentTag());//
            holder.getTextView(R.id.konw_title).setText(itemData.getContentName());
            holder.itemView.setVisibility(View.VISIBLE);
        } else {//隐藏过期的数据
            holder.itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
            holder.itemView.setLayoutParams(param);

        }else {}
        }



}

