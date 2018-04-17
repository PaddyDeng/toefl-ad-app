package io.dcloud.H58E83894.ui.prelesson.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.utils.HtmlUtil;

import static io.dcloud.H58E83894.weiget.DateUtils.timeslashData;


public class DownloadAdapter extends BaseRecyclerViewAdapter<DownloadData> {

    private int indexs;
//    List<DownloadData> dataList = new ArrayList<>();
    public DownloadAdapter(Context context, List<DownloadData> data, RecyclerView.LayoutManager mLayoutManager, int index) {
        super(context, data, mLayoutManager);
        indexs = index;
        Log.d("ooo", "bindItemViewData1: "+index+indexs);
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }



    private OnItemClickLitener mOnItemClickLitener;

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.down_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {

            return 0;
    }

//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, DownloadData itemData) {
       {
           Log.d("ooo", "bindItemViewData: "+indexs);
           if ( indexs == 0){//考试
               holder.getTextView(R.id.download_item_title_tv).setText(itemData.getTitle());
               holder.getTextView(R.id.post_up_person_tv).setText(R.string.str_post_name);
               String dateTime = timeslashData(itemData.getCreateTime());
               holder.getTextView(R.id.up_time_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_time, dateTime)));
               holder.getTextView(R.id.see_person_num_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_see, itemData.getViewCount())));
               holder.getTextView(R.id.down_status).setText(R.string.str_see);
           }else if ( indexs == 1){//高分
//               holder.getTextView(R.id.download_item_title_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_item_download_type_02, itemData.getTitle())));
               holder.getTextView(R.id.download_item_title_tv).setText(itemData.getTitle());
               holder.getTextView(R.id.see_person_num_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_see, itemData.getViewCount())));
               String name = itemData.getUsername();
               if (!TextUtils.isEmpty(itemData.getNickname())) {
                   name = itemData.getNickname();
               }
               holder.getTextView(R.id.up_time_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_time, itemData.getDateTime())));
               holder.getTextView(R.id.post_up_person_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_person, name)));
               holder.getTextView(R.id.down_item_des).setVisibility(View.GONE);
               holder.getTextView(R.id.down_status).setText(R.string.str_see);
           }else if ( indexs == 2){//资料下载
//               holder.getTextView(R.id.download_item_title_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_item_download_type_03, itemData.getTitle())));
               holder.getTextView(R.id.download_item_title_tv).setText(itemData.getTitle());
               holder.getTextView(R.id.down_item_des).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_item_download_person_num, itemData.getViewCount())));
               String name = itemData.getUsername();
               if (!TextUtils.isEmpty(itemData.getNickname())) {
                   name = itemData.getNickname();
               }
               holder.getTextView(R.id.up_time_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_time, itemData.getDateTime())));
               holder.getTextView(R.id.post_up_person_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_person, name)));
               holder.getTextView(R.id.down_item_des).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_item_download_person_num, itemData.getViewCount())));
           }else if ( indexs== 3){//鸡精
//               holder.getTextView(R.id.download_item_title_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_item_download_type, itemData.getTitle())));
               holder.getTextView(R.id.download_item_title_tv).setText(itemData.getTitle());
               holder.getTextView(R.id.post_up_person_tv).setText(R.string.str_post_name);
               String dateTime = timeslashData(itemData.getCreateTime());
               holder.getTextView(R.id.up_time_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_time, dateTime)));
               holder.getTextView(R.id.see_person_num_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_see, itemData.getViewCount())));
               holder.getTextView(R.id.down_status).setText(R.string.str_see);
           }else if ( indexs == 4){//口语
//               holder.getTextView(R.id.download_item_title_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_item_download_type_04, itemData.getTitle())));
               holder.getTextView(R.id.download_item_title_tv).setText(itemData.getTitle());
               holder.getTextView(R.id.post_up_person_tv).setText(R.string.str_post_name);
               String dateTime = timeslashData(itemData.getCreateTime());
               holder.getTextView(R.id.up_time_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_time, dateTime)));
               holder.getTextView(R.id.see_person_num_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_see, itemData.getViewCount())));
               holder.getTextView(R.id.down_status).setText(R.string.str_see);
           }else if ( indexs == 5){//作文
//               holder.getTextView(R.id.download_item_title_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_item_download_type_05, itemData.getTitle())));
               holder.getTextView(R.id.download_item_title_tv).setText(itemData.getTitle());
               holder.getTextView(R.id.post_up_person_tv).setText(R.string.str_post_name);
               String dateTime = timeslashData(itemData.getCreateTime());
               holder.getTextView(R.id.up_time_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_time, dateTime)));
               holder.getTextView(R.id.see_person_num_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_see, itemData.getViewCount())));
               holder.getTextView(R.id.down_status).setText(R.string.str_see);
           } else {

               // HtmlUtil.fromHtml(mContext.getString(R.string.select_confim, name))
           }

        }
    }
}
