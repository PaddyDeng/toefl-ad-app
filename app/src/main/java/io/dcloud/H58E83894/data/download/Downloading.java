package io.dcloud.H58E83894.data.download;

import android.graphics.Bitmap;


import io.dcloud.H58E83894.ui.toeflcircle.adapter.DownloadListAdapter;
import io.reactivex.disposables.Disposable;
import zlc.season.rxdownload2.entity.DownloadFlag;

public class Downloading implements DownloadType {
    private String url;
    private String title;
    private int flag = DownloadFlag.NORMAL;
    public Disposable mDisposable;
    public Bitmap downloading;
    public Bitmap pauseDownload;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getType() {
        return DownloadListAdapter.DOWNLOADING_CONTENT;
    }


}
