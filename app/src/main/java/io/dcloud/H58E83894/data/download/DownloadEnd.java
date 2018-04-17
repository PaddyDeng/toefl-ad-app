package io.dcloud.H58E83894.data.download;


import io.dcloud.H58E83894.ui.toeflcircle.adapter.DownloadListAdapter;

public class DownloadEnd implements DownloadType {
    private String title;
    private String savePath;
    private String fileSize;
    private String url;
    private long downloadTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    @Override
    public int getType() {
        return DownloadListAdapter.DOWNLOAD_END_CONTENT;
    }
}
