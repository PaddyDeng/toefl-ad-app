package io.dcloud.H58E83894.data.download;


import io.dcloud.H58E83894.ui.toeflcircle.adapter.DownloadListAdapter;

public class DownloadingTitle implements DownloadType {
    private String mDownloadingTitle;

    public String getDownloadingTitle() {
        return mDownloadingTitle;
    }

    public void setDownloadingTitle(String downloadingTitle) {
        mDownloadingTitle = downloadingTitle;
    }

    @Override
    public int getType() {
        return DownloadListAdapter.DOWNLOADING_TITLE;
    }
}
