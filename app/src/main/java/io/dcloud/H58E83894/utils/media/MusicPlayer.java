package io.dcloud.H58E83894.utils.media;

import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.dcloud.H58E83894.callback.ICallBackImp;
import io.dcloud.H58E83894.data.MediaData;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by fire on 2017/7/19  17:38.
 */

public class MusicPlayer implements MediaPlayer.OnCompletionListener {
//    private static MusicPlayer player = new MusicPlayer();

    private MediaPlayer mMediaPlayer;
    private List<MediaData> mQueue;
    private int mQueueIndex;
    private PlayMode mPlayMode;
    private MediaData currMediaData;

    private MediaPlayer.OnCompletionListener mCompletionListener;

    public void setCompletionListener(MediaPlayer.OnCompletionListener completionListener) {
        mCompletionListener = completionListener;
    }

    private enum PlayMode {
        LOOP, RANDOM, REPEAT
    }





    public MusicPlayer() {

        mMediaPlayer = new ManagedMediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);

        mQueue = new ArrayList<>();
        mQueueIndex = 0;

        mPlayMode = PlayMode.LOOP;
    }

//    ====================================top===================================

    public int getCurrPosition() {
        if (currMediaData != null)
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }

    public void play(RxPermissions mRxPermissions, RxDownload mRxDownload,
                     String url, ICallBackImp imp, MediaPlayer.OnCompletionListener listener) {
        play(mRxPermissions, mRxDownload, url, 0, null, imp, listener);
    }

    public void play(RxPermissions mRxPermissions, RxDownload mRxDownload,
                     String url, View v, ICallBackImp imp, MediaPlayer.OnCompletionListener listener) {
        play(mRxPermissions, mRxDownload, url, 0, v, imp, listener);
    }

    public void play(RxPermissions mRxPermissions, RxDownload mRxDownload,
                     String url, int progress, View v, ICallBackImp imp) {
        play(mRxPermissions, mRxDownload, url, progress, v, imp, null);
    }

    public void play(RxPermissions mRxPermissions, final RxDownload mRxDownload,
                     final String url, final int progress, final View v, final ICallBackImp imp,
                     final MediaPlayer.OnCompletionListener listener) {
        mRxPermissions.request(READ_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("no permission");
                        }
                    }
                }).compose(new ObservableTransformer<Boolean, DownloadRecord>() {
            @Override
            public ObservableSource<DownloadRecord> apply(Observable<Boolean> upstream) {
                return upstream.flatMap(new Function<Boolean, ObservableSource<DownloadRecord>>() {
                    @Override
                    public ObservableSource<DownloadRecord> apply(@NonNull Boolean aBoolean) throws Exception {
                        return mRxDownload.getDownloadRecord(url);
                    }
                });
            }
        }).compose(new SchedulerTransformer<DownloadRecord>())
                .subscribe(new Consumer<DownloadRecord>() {
                    @Override
                    public void accept(@NonNull DownloadRecord record) throws Exception {
                        MediaData data = new MediaData();
                        data.setName(record.getSaveName());
                        data.setPath(TextUtils.concat(record.getSavePath(), File.separator, record.getSaveName()).toString());
                        seekToPlay(data, progress);
                        setCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if (listener != null)
                                    listener.onCompletion(mp);
                                if (v != null)
                                    v.setSelected(!v.isSelected());
                            }
                        });
                        if (imp != null)
                            imp.onSuccess("");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (v != null)
                            v.setSelected(!v.isSelected());
                        if (imp != null)
                            imp.onFail();
                    }
                });
    }

    private void seekToPlay(MediaData song, final int progress) {
        try {
            currMediaData = song;
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(song.getPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.seekTo(progress);
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null)
            return mMediaPlayer.isPlaying();
        return false;
    }

    public void pause() {
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    public void seekTo(int progress) {
        if (mMediaPlayer != null)
            mMediaPlayer.seekTo(progress);
    }

    public void resume() {
        mMediaPlayer.start();
    }

    //    ===================================bottom=======================================
    public void setQueue(List<MediaData> queue, int index) {
        mQueue.clear();
        mQueue.addAll(queue);
//        mQueue = queue;
        mQueueIndex = index;
        play(getNowPlaying());
    }

    public void setQueue(List<MediaData> queue, int index, int progress) {
        mQueue.clear();
        mQueue.addAll(queue);
//        mQueue = queue;
        mQueueIndex = index;
        seekToPlay(getNowPlaying(), progress);
    }


    public void play(MediaData song) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(song.getPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getMPSpeed(float speed){

        mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
    }

    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void next() {
        play(getNextSong());
    }

    public void previous() {
        play(getPreviousSong());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        next();
        if (mCompletionListener != null)
            mCompletionListener.onCompletion(mp);
    }

    private MediaData getNowPlaying() {
        if (mQueue.isEmpty()) {
            return null;
        }
        return mQueue.get(mQueueIndex);
    }

    private MediaData getNextSong() {
        if (mQueue.isEmpty()) {
            return null;
        }
        switch (mPlayMode) {
            case LOOP:
                return mQueue.get(getNextIndex());
            case RANDOM:
                return mQueue.get(getRandomIndex());
            case REPEAT:
                return mQueue.get(mQueueIndex);
        }
        return null;
    }

    private MediaData getPreviousSong() {
        if (mQueue.isEmpty()) {
            return null;
        }
        switch (mPlayMode) {
            case LOOP:
                return mQueue.get(getPreviousIndex());
            case RANDOM:
                return mQueue.get(getRandomIndex());
            case REPEAT:
                return mQueue.get(mQueueIndex);
        }
        return null;
    }

    public int getCurrentPosition() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    private int getNextIndex() {
        mQueueIndex = (mQueueIndex + 1) % mQueue.size();
        return mQueueIndex;
    }

    private int getPreviousIndex() {
        mQueueIndex = (mQueueIndex - 1) % mQueue.size();
        return mQueueIndex;
    }

    private int getRandomIndex() {
        mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
        return mQueueIndex;
    }

    public void release() {
        mMediaPlayer.release();
        mMediaPlayer = null;
//        player = null;
    }
}
