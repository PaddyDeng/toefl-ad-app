package io.dcloud.H58E83894.data.download;


import io.dcloud.H58E83894.ui.toeflcircle.adapter.DownloadListAdapter;

public class DownloadEndTitle implements DownloadType {
    private String mDownloadEndTitle;

    public String getDownloadEndTitle() {
        return mDownloadEndTitle;
    }

    public void setDownloadEndTitle(String downloadEndTitle) {
        mDownloadEndTitle = downloadEndTitle;
    }

    @Override
    public int getType() {
        return DownloadListAdapter.DOWNLOAD_END_TITLE;
    }
}
