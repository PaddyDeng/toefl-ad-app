package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.make.ListenChildData;
import io.dcloud.H58E83894.data.make.ListenSecRecordData;
import io.dcloud.H58E83894.data.make.ListenTpoContentData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by fire on 2017/8/7  15:18.
 */

public class ListenSecAdapter extends BaseRecyclerViewAdapter<ListenTpoContentData> {

    private RxDownload mRxDownload;
    private RxPermissions mRxPermissions;

    public ListenSecAdapter(Context context, List<ListenTpoContentData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        mRxDownload = RxDownload.getInstance(context);
    }

    public void setRxPermissions(RxPermissions rxPermissions) {
        mRxPermissions = rxPermissions;
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.listen_sec_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, ListenTpoContentData itemData) {
        holder.getTextView(R.id.listen_sec_item_title).setText(itemData.getName());
        List<ListenChildData> child = itemData.getChild();
        List<ListenSecRecordData> record = itemData.getRecord();
        if (record == null || record.isEmpty()) {
            holder.getTextView(R.id.listen_sec_item_make_info).setText(
                    mContext.getString(R.string.str_sec_listen_item_des, "0%", "0h"));
        } else {
            int correctNum = 0;
            int useTime = 0;
            for (ListenSecRecordData lsrd : record) {
                if (lsrd.getAnswer().equals(lsrd.getTrueAnswer())) {
                    correctNum++;
                }
                useTime += Integer.parseInt(lsrd.getElapsedTime());
            }
            String avg = Math.round(correctNum * 1.0f / child.size() * 100) + "%";
            holder.getTextView(R.id.listen_sec_item_make_info).setText(
                    mContext.getString(R.string.str_sec_listen_item_des, avg, Utils.format(useTime)));
        }
        final TextView downStatus = holder.getTextView(R.id.listen_sec_item_down_status);
        final ImageView nextImg = holder.getImageView(R.id.listen_sec_item_next_iv);

        Utils.setVisible(downStatus);
        Utils.setGone(nextImg);

        final String url = RetrofitProvider.TOEFLURL + itemData.getFile();
        downStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadUtil.downloadService(url, mRxDownload, mRxPermissions);
            }
        });
        itemData.mDisposable = mRxDownload.receiveDownloadStatus(url)
                .subscribe(new Consumer<DownloadEvent>() {
                    @Override
                    public void accept(@NonNull DownloadEvent event) throws Exception {
                        if (event.getFlag() == DownloadFlag.COMPLETED) {
                            Utils.setVisible(nextImg);
                            Utils.setGone(downStatus);
                        } else if (event.getFlag() != DownloadFlag.NORMAL) {
                            Utils.setVisible(downStatus);
                            Utils.setGone(nextImg);
                            DownloadStatus status = event.getDownloadStatus();
                            downStatus.setText(status.getPercentNumber() + "%");
                        }
                    }
                });
    }
}
