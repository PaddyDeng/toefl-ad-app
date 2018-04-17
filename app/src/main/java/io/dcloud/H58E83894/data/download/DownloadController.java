package io.dcloud.H58E83894.data.download;



import io.dcloud.H58E83894.weiget.DownloadScheduleView;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

public class DownloadController {

    private DownloadState mState;
    private DownloadScheduleView mScheduleView;

    public DownloadController(DownloadScheduleView downloadScheduleView) {
        mScheduleView = downloadScheduleView;
        setState(new Normal());
    }


    public void handleClick(Callback callback) {
        mState.handleClick(callback);
    }

    public void setEvent(DownloadEvent event) {
        int flag = event.getFlag();
        switch (flag) {
            case DownloadFlag.NORMAL:
                //未下载 先不管
                mScheduleView.setPause(true);
                setState(new DownloadController.Normal());
                break;
            case DownloadFlag.WAITING:
                mScheduleView.setPause(true);
                setState(new DownloadController.Waiting());
                break;
            case DownloadFlag.STARTED:
                mScheduleView.setPause(false);
                setState(new DownloadController.Started());
                break;
            case DownloadFlag.PAUSED:
                mScheduleView.setPause(true);
                setState(new DownloadController.Paused());
                break;
            case DownloadFlag.COMPLETED:
                mScheduleView.setPause(true);
                setState(new DownloadController.Completed());
                break;
            case DownloadFlag.FAILED:
                mScheduleView.setPause(true);
                setState(new DownloadController.Failed());
                break;
            case DownloadFlag.DELETED:
                mScheduleView.setPause(true);
                setState(new DownloadController.Deleted());
                break;
        }
    }

    public void setState(DownloadState state) {
        mState = state;
    }

    public interface Callback {
        void startDownload();

        void pauseDownload();

        void completedDownload();
    }

    static abstract class DownloadState {

        abstract void handleClick(Callback callback);
    }

    public static class Normal extends DownloadState {
        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }

    public static class Waiting extends DownloadState {
        @Override
        void handleClick(Callback callback) {
            callback.pauseDownload();
        }
    }

    public static class Started extends DownloadState {
        @Override
        void handleClick(Callback callback) {
            callback.pauseDownload();
        }
    }

    public static class Paused extends DownloadState {
        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }

    public static class Failed extends DownloadState {
        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }


    public static class Completed extends DownloadState {
        @Override
        void handleClick(Callback callback) {
            callback.completedDownload();
        }
    }

    public static class Deleted extends DownloadState {
        @Override
        void handleClick(Callback callback) {
            callback.startDownload();
        }
    }
}
