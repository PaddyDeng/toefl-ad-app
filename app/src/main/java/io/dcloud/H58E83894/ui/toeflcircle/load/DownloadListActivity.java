package io.dcloud.H58E83894.ui.toeflcircle.load;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.download.DownloadEnd;
import io.dcloud.H58E83894.data.download.DownloadEndTitle;
import io.dcloud.H58E83894.data.download.DownloadType;
import io.dcloud.H58E83894.data.download.Downloading;
import io.dcloud.H58E83894.data.download.DownloadingTitle;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.toeflcircle.adapter.DownloadListAdapter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.db.DataBaseHelper;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

import zlc.season.rxdownload2.function.Utils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class DownloadListActivity extends BaseActivity {

    @BindView(R.id.download_list_recycle)
    RecyclerView mRecyclerView;

    private List<DownloadType> mDownloadTypeList;
    private DownloadListAdapter mAdapter;
    private RxDownload mRxDownload;
    private String currentUrl;
    private String currentTitle;

    public static void startDownloadAct(Context c, String url, String title) {
        Intent intent = new Intent(c, DownloadListActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, url);
        intent.putExtra(Intent.EXTRA_TEXT, title);
        c.startActivity(intent);
    }

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        currentUrl = intent.getStringExtra(Intent.EXTRA_INDEX);
        currentTitle = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mRxDownload = RxDownload.getInstance(mContext);
        //在这里初始化mAdapter
        mRxPermissions.request(READ_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("no permission");
                        }
                    }
                }).compose(new ObservableTransformer<Boolean, List<DownloadRecord>>() {
            @Override
            public ObservableSource<List<DownloadRecord>> apply(Observable<Boolean> upstream) {
                return upstream.flatMap(new Function<Boolean, ObservableSource<List<DownloadRecord>>>() {
                    @Override
                    public ObservableSource<List<DownloadRecord>> apply(@NonNull Boolean aBoolean) throws Exception {
                        return mRxDownload.getTotalDownloadRecords();
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<DownloadRecord>>())
                .subscribe(new Consumer<List<DownloadRecord>>() {
                    @Override
                    public void accept(@NonNull List<DownloadRecord> records) throws Exception {
                        initAdapter(records);
                    }
                });
    }

    private void initAdapter(List<DownloadRecord> records) {
        if (mDownloadTypeList != null) {
            mDownloadTypeList.clear();
        }
        mDownloadTypeList = new ArrayList<>();
        DownloadingTitle loadingTitle = new DownloadingTitle();//正在下载标题
        DownloadEndTitle endTitle = new DownloadEndTitle();//已完成标题
        List<DownloadType> loading = new ArrayList<>();//存储正在下载
        List<DownloadType> dlEnd = new ArrayList<>();//存储下载完成
        boolean hasCurrentUrl = false;
        for (DownloadRecord record : records) {
            if (!hasCurrentUrl) {
                hasCurrentUrl = TextUtils.equals(record.getUrl(), currentUrl) ? true : false;
            }
            String filePath = TextUtils.concat(record.getSavePath(), File.separator, record.getSaveName()).toString();
            if (record.getFlag() == DownloadFlag.COMPLETED) {
                File file = new File(filePath);
                if (file.exists()) {
                    DownloadEnd de = new DownloadEnd();
                    de.setDownloadTime(file.lastModified());
                    de.setFileSize(Utils.formatSize(file.length()));
                    de.setTitle(record.getExtra1());
                    de.setUrl(record.getUrl());
                    de.setSavePath(filePath);
                    dlEnd.add(de);
                } else {
                    if (TextUtils.equals(record.getUrl(), currentUrl)) {//当前下载记录被删除
                        DataBaseHelper.getSingleton(mContext).deleteRecord(currentUrl);
                        hasCurrentUrl = false;
                    }
//                    else {
//                        DataBaseHelper.getSingleton(mContext).deleteRecord(record.getUrl());
//                        Downloading deing = new Downloading();
//                        deing.setUrl(record.getUrl());
//                        deing.setTitle(record.getExtra1());
//                        deing.setFlag(record.getFlag());
//                        loading.add(deing);
//                    }
                }
            } else {
                File file = new File(filePath);
                if (file.exists()) {
                    Downloading deing = new Downloading();
                    deing.setUrl(record.getUrl());
                    deing.setFlag(record.getFlag());
                    deing.setTitle(record.getExtra1());
                    loading.add(deing);
                }
            }
        }
        if (!hasCurrentUrl) {
            Downloading ding = new Downloading();
            ding.setUrl(currentUrl);
            ding.setTitle(currentTitle);
            ding.setFlag(DownloadFlag.NORMAL);
            loading.add(0, ding);
        }
        loadingTitle.setDownloadingTitle(getString(R.string.str_downloading_des, loading.size()));
        endTitle.setDownloadEndTitle(getString(R.string.str_download_end_des, dlEnd.size()));
        mDownloadTypeList.add(loadingTitle);
        mDownloadTypeList.addAll(loading);
        mDownloadTypeList.add(endTitle);
        mDownloadTypeList.addAll(dlEnd);

        mAdapter = new DownloadListAdapter(mDownloadTypeList, mRxPermissions, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //需要释放下载列表中默认加载的两张图片

        //取消订阅
        if (mDownloadTypeList != null) {
            for (DownloadType type : mDownloadTypeList) {
                if (type.getType() == DownloadListAdapter.DOWNLOADING_CONTENT) {
                    Disposable disposable = ((Downloading) type).mDisposable;
                    if (disposable != null && !disposable.isDisposed()) {
                        disposable.dispose();
                    }
                } else if (type.getType() == DownloadListAdapter.DOWNLOADING_CONTENT) {
                    Downloading downloading = (Downloading) type;
                    if (downloading.pauseDownload != null) {
                        downloading.pauseDownload.recycle();
                        downloading.pauseDownload = null;
                    }
                    if (downloading.downloading != null) {
                        downloading.downloading.recycle();
                        downloading.downloading = null;
                    }
                }
            }
        }
    }
}
