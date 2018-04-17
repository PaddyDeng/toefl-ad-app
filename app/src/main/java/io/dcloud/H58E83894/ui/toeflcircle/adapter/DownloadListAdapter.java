package io.dcloud.H58E83894.ui.toeflcircle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.download.DownloadController;
import io.dcloud.H58E83894.data.download.DownloadEnd;
import io.dcloud.H58E83894.data.download.DownloadEndTitle;
import io.dcloud.H58E83894.data.download.DownloadType;
import io.dcloud.H58E83894.data.download.Downloading;
import io.dcloud.H58E83894.data.download.DownloadingTitle;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.utils.OpenFileUtil;
import io.dcloud.H58E83894.utils.TimeUtils;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.DownloadScheduleView;
import io.dcloud.H58E83894.weiget.ItemSlideHelper;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.db.DataBaseHelper;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;
import zlc.season.rxdownload2.entity.DownloadStatus;


import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class DownloadListAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> implements ItemSlideHelper.Callback {

    public static final int DOWNLOADING_TITLE = 100;//显示正在下载
    public static final int DOWNLOADING_CONTENT = DOWNLOADING_TITLE + 1;//显示下载内容
    public static final int DOWNLOAD_END_TITLE = DOWNLOADING_CONTENT + 1;//显示已下载完成
    public static final int DOWNLOAD_END_CONTENT = DOWNLOAD_END_TITLE + 1;//显示已下载完成内容

    private RecyclerView mRecyclerView;
    private List<DownloadType> mDatas;
    private RxPermissions mRxpermissions;
    private RxDownload mRxDownload;
    private Context mContext;

    public DownloadListAdapter(List<DownloadType> datas, RxPermissions mRxpermissions, Context mContext) {
        mDatas = datas;
        this.mContext = mContext;
        this.mRxpermissions = mRxpermissions;
        mRxDownload = RxDownload.getInstance(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == DOWNLOADING_TITLE) {
            return new BaseRecyclerViewHolder(context, LayoutInflater.from(context).inflate(R.layout.download_title_des, parent, false));
        } else if (viewType == DOWNLOADING_CONTENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.downloading_layout, parent, false);
            return new BaseRecyclerViewHolder(context, view);
        } else if (viewType == DOWNLOAD_END_TITLE) {
            return new BaseRecyclerViewHolder(context, LayoutInflater.from(context).inflate(R.layout.download_title_des, parent, false));
        } else if (viewType == DOWNLOAD_END_CONTENT) {
            return new BaseRecyclerViewHolder(context, LayoutInflater.from(context).inflate(R.layout.download_end_layout, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == DOWNLOADING_TITLE) {
            DownloadingTitle downloadTitle = (DownloadingTitle) mDatas.get(position);
            holder.getTextView(R.id.download_title_tip).setText(downloadTitle.getDownloadingTitle());
        } else if (type == DOWNLOADING_CONTENT) {//涉及删除
            downloading(holder, position);
        } else if (type == DOWNLOAD_END_TITLE) {
            DownloadEndTitle endTitle = (DownloadEndTitle) mDatas.get(position);
            holder.getTextView(R.id.download_title_tip).setText(endTitle.getDownloadEndTitle());
        } else if (type == DOWNLOAD_END_CONTENT) {//涉及删除
            downloadEndShow(holder, position);
        }
    }

    private void downloadEndShow(BaseRecyclerViewHolder holder, int position) {
        final DownloadEnd downloadEnd = (DownloadEnd) mDatas.get(position);//下载完成
        holder.getTextView(R.id.download_end_title).setText(downloadEnd.getTitle());
        holder.getTextView(R.id.download_end_file_size).setText(downloadEnd.getFileSize());
        holder.getTextView(R.id.download_end_file_modify).setText(TimeUtils.longToString(downloadEnd.getDownloadTime(), "yyyy.MM.dd"));
        holder.getView(R.id.download_end_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(downloadEnd.getSavePath());
            }
        });
        holder.getTextView(R.id.download_end_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper.getSingleton(mContext).deleteRecord(downloadEnd.getUrl());
                File file = new File(downloadEnd.getSavePath());
                if (file.exists()) {
                    file.delete();
                }
                updateAdapter();
            }
        });
    }

    private void openFile(String path) {
        File file = new File(path);
//        if (file.exists()) {
//            Intent intent = new Intent("android.intent.action.VIEW");
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Uri uri = Uri.fromFile(file);
//            intent.setDataAndType(uri, "application/pdf");
//            mContext.startActivity(intent);
        OpenFileUtil.openFile(file, mContext);
//        } else {
//            Utils.toastShort(mContext, R.string.str_file_no_exists);
//        }
    }

    private void downloading(final BaseRecyclerViewHolder holder, int position) {
        final Downloading downloading = (Downloading) mDatas.get(position);//正在下载
        holder.getTextView(R.id.downloading_title_tv).setText(downloading.getTitle());
        final TextView scheduleTextView = holder.getTextView(R.id.downloading_schedule_tv);
        final DownloadScheduleView downloadScheduleView = holder.getView(R.id.downloading_btn);
        final DownloadController mDownloadController = new DownloadController(downloadScheduleView);
        downloading.downloading = downloadScheduleView.downloading;
        downloading.pauseDownload = downloadScheduleView.pauseLoading;
        final String url = downloading.getUrl();

        final DownloadBean downloadBean = new DownloadBean.Builder(url)
                .setExtra1(downloading.getTitle())
                .build();
        if (downloading.getFlag() == DownloadFlag.NORMAL) {
            start(downloadBean);
        }
        downloading.mDisposable = mRxDownload.receiveDownloadStatus(url)
                .subscribe(new Consumer<DownloadEvent>() {
                    @Override
                    public void accept(@NonNull DownloadEvent downloadEvent) throws Exception {
                        mDownloadController.setEvent(downloadEvent);
                        updateProgressStatus(downloadEvent, scheduleTextView, downloadScheduleView);
                        if (downloadEvent.getFlag() == DownloadFlag.COMPLETED) {
                            //更新adapter
                            updateAdapter();
                        }
                    }
                });

        holder.getTextView(R.id.downloading_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloading.mDisposable != null && !downloading.mDisposable.isDisposed()) {
                    downloading.mDisposable.dispose();
                }
                DataBaseHelper.getSingleton(mContext).deleteRecord(downloading.getUrl());
                File[] files = mRxDownload.getRealFiles(downloading.getUrl());
                if (files != null) {
                    if (files[0].exists()) {
                        files[0].delete();
                    }
                }
                updateAdapter();
            }
        });

        //点击事件
        downloadScheduleView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDownloadController.handleClick(new DownloadController.Callback() {
                    @Override
                    public void startDownload() {
                        start(downloadBean);
                    }

                    @Override
                    public void pauseDownload() {
                        pause(url);
                    }

                    @Override
                    public void completedDownload() {
//                        updateAdapter();
                    }
                });
            }
        });
    }

    private void updateAdapter() {
        for (DownloadType type : mDatas) {
            if (type.getType() == DownloadListAdapter.DOWNLOADING_CONTENT) {
                Disposable disposable = ((Downloading) type).mDisposable;
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        }
        mDatas.clear();
        mRxDownload.getTotalDownloadRecords()
                .compose(new SchedulerTransformer<List<DownloadRecord>>())
                .subscribe(new Consumer<List<DownloadRecord>>() {
                    @Override
                    public void accept(@NonNull List<DownloadRecord> records) throws Exception {
                        DownloadingTitle loadingTitle = new DownloadingTitle();//正在下载标题
                        DownloadEndTitle endTitle = new DownloadEndTitle();//已完成标题
                        List<DownloadType> loading = new ArrayList<>();//存储正在下载
                        List<DownloadType> dlEnd = new ArrayList<>();//存储下载完成
                        Utils.logh("record size ", " " + records.size());
//                        Collections.reverse(records);
                        for (DownloadRecord record : records) {
                            String filePath = TextUtils.concat(record.getSavePath(), File.separator, record.getSaveName()).toString();
                            if (record.getFlag() == DownloadFlag.COMPLETED) {
                                Utils.logh("record size ", "COMPLETED ");
                                File file = new File(filePath);
                                DownloadEnd de = new DownloadEnd();
                                de.setDownloadTime(file.lastModified());
                                de.setFileSize(zlc.season.rxdownload2.function.Utils.formatSize(file.length()));
                                de.setTitle(record.getExtra1());
                                de.setUrl(record.getUrl());
                                de.setSavePath(filePath);
                                dlEnd.add(de);
                            } else {
//                                File file = new File(filePath);
                                Utils.logh("record size ", "no COMPLETED ");
                                Downloading deing = new Downloading();
                                deing.setUrl(record.getUrl());
                                deing.setFlag(record.getFlag());
                                deing.setTitle(record.getExtra1());
                                loading.add(deing);
                            }
                        }
                        loadingTitle.setDownloadingTitle(mContext.getString(R.string.str_downloading_des, loading.size()));
                        endTitle.setDownloadEndTitle(mContext.getString(R.string.str_download_end_des, dlEnd.size()));
                        mDatas.add(loadingTitle);
                        mDatas.addAll(loading);
                        mDatas.add(endTitle);
                        mDatas.addAll(dlEnd);
                        notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void updateProgressStatus(DownloadEvent event, TextView scheduleTextView, DownloadScheduleView downloadScheduleView) {
        DownloadStatus downloadStatus = event.getDownloadStatus();
        scheduleTextView.setText(downloadStatus.getFormatStatusString());
        downloadScheduleView.setProgress(downloadStatus.getPercentNumber() * 1.0f);
    }

    private void pause(String url) {
        mRxDownload.pauseServiceDownload(url).subscribe();
    }

    private void start(DownloadBean bean) {
        mRxpermissions.request(WRITE_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("no permission");
                        }
                    }
                })
                .compose(mRxDownload.<Boolean>transformService(bean))
                .subscribe();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mRecyclerView.getContext(), this));
    }

    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            if (viewGroup.getChildCount() == 2) {
                return viewGroup.getChildAt(1).getLayoutParams().width;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return mRecyclerView.getChildViewHolder(childView);
    }

    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }
}
