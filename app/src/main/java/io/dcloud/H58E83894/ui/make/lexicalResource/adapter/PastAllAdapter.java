package io.dcloud.H58E83894.ui.make.lexicalResource.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.make.AllTaskData;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import zlc.season.rxdownload2.RxDownload;

import static io.dcloud.H58E83894.ui.make.easyResource.EaAllDetailActivity.startEaAllDetailActivity;
import static io.dcloud.H58E83894.ui.make.lexicalResource.AllDetailActivity.startAllDetailActivity;


public class PastAllAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<AllTaskData.DataBean> share;
    private Context context;


    public PastAllAdapter(List<AllTaskData.DataBean> share, Context context, RxPermissions rxPermissions) {
        this.share = share;
        this.context = context;
    }

    public List<AllTaskData.DataBean> getDate() {
        return share;
    }

    public void update(List<AllTaskData.DataBean> lists) {
        if (lists == null || lists.isEmpty()) return;
        share = lists;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_all_item, parent, false);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {

        final AllTaskData.DataBean itemData = share.get(position);
        if(TextUtils.isEmpty(itemData.toString()) || (itemData.toString().trim()).equals("null")){ return;}
        holder.getTextView(R.id.today_free).setText(itemData.getTime());//日期批改
        holder.getTextView(R.id.today_speak_sum).setText(HtmlUtil.fromHtml(context.getString(R.string.str_corret_times, itemData.getCount()+"")));//多少人参与
        holder.getTextView(R.id.today_speak_title).setText(itemData.getQuestion());

        holder.getView(R.id.corret_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startAllDetailActivity(context, itemData);

            }
        });
    }

    @Override
    public int getItemCount() {
       return share == null ? 0 : share.size();
    }



}
