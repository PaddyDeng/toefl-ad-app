package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.ICallBackImp;
import io.dcloud.H58E83894.data.MediaData;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
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
 * Created by Fire on 2017/7/28.
 */
public class PlayAudioView extends RelativeLayout {

    private Context mContext;
    private ImageView playImageView;
    private SeekBar playSeekBar;
    private TextView playTime;
    private boolean canPlay = false;//能否播放
    private MusicPlayer mPlayer;
    private String url;
    private RxPermissions mRxPermissions;
    private RxDownload mRxDownload;
    private boolean addToPlayer = false;//是否已经添加到musicplayer

    public PlayAudioView(Context context) {
        this(context, null);
    }

    public PlayAudioView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayAudioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.play_audio_view_layout, null, false);
        addView(view);
        playImageView = (ImageView) view.findViewById(R.id.play_status_img);
        playSeekBar = (SeekBar) view.findViewById(R.id.play_seekbar);
        playTime = (TextView) view.findViewById(R.id.play_time_tv);
        initListener();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mPlayer != null) {
                int position = mPlayer.getCurrPosition();
                playSeekBar.setProgress(position);
                playTime.setText(Utils.formatTime(position));
            }
            mHandler.sendEmptyMessageDelayed(1, 100);
            return false;
        }
    });

    public void setInit(boolean canPlay, int max, String url, RxPermissions rxPermission, RxDownload rxDownload) {
        this.canPlay = canPlay;
        mRxDownload = rxDownload;
        this.url = url;
        mRxPermissions = rxPermission;
        playSeekBar.setMax(max);
        if (mPlayer == null)
            mPlayer = new MusicPlayer();
    }

    public void onDetach() {
        mHandler.removeCallbacksAndMessages(null);
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private boolean canPlay() {
        if (!canPlay) {
            Utils.toastShort(mContext, R.string.str_src_downloading);
            return false;
        }
        return true;
    }

    private void initListener() {
        playImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canPlay()) return;
                play(v);
            }
        });
        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (!canPlay()) return;
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    playImageView.setSelected(playImageView.isSelected());
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!canPlay) {
                    seekBar.setProgress(0);
                    return;
                }
                if (mPlayer != null && canPlay) {
                    //获取当前拖拽的位置
                    int dragedProgress = seekBar.getProgress();
                    //记录拖拽的位置
                    mPlayer.seekTo(dragedProgress);
                    playTime.setText(Utils.formatTime(dragedProgress));
                    mPlayer.resume();
                    playImageView.setSelected(true);
                }
            }
        });
    }

    private void play(View v) {
        if (mPlayer == null) return;
        v.setSelected(!v.isSelected());
        if (!addToPlayer) {
            initMusicPlay(v);
            return;
        }
        if (v.isSelected()) {
            mPlayer.resume();
        } else {
            mPlayer.pause();
        }
    }


    private void initMusicPlay(final View v) {
        mHandler.sendEmptyMessageDelayed(1, 100);
        mPlayer.play(mRxPermissions, mRxDownload, url, 0, v, new ICallBackImp() {
            @Override
            public void onSuccess(Object o) {
                addToPlayer = true;
            }
        });

//        mRxPermissions.request(READ_EXTERNAL_STORAGE)
//                .doOnNext(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(@NonNull Boolean aBoolean) throws Exception {
//                        if (!aBoolean) {
//                            throw new RuntimeException("no permission");
//                        }
//                    }
//                }).compose(new ObservableTransformer<Boolean, DownloadRecord>() {
//            @Override
//            public ObservableSource<DownloadRecord> apply(Observable<Boolean> upstream) {
//                return upstream.flatMap(new Function<Boolean, ObservableSource<DownloadRecord>>() {
//                    @Override
//                    public ObservableSource<DownloadRecord> apply(@NonNull Boolean aBoolean) throws Exception {
//                        return mRxDownload.getDownloadRecord(url);
//                    }
//                });
//            }
//        }).compose(new SchedulerTransformer<DownloadRecord>())
//                .subscribe(new Consumer<DownloadRecord>() {
//                    @Override
//                    public void accept(@NonNull DownloadRecord record) throws Exception {
//                        List<MediaData> list = new ArrayList<MediaData>();
//                        MediaData data = new MediaData();
//                        data.setName(record.getSaveName());
//                        data.setPath(TextUtils.concat(record.getSavePath(), File.separator, record.getSaveName()).toString());
//                        list.add(data);
//                        mPlayer.setQueue(list, list.size() - 1);
//                        addToPlayer = true;
//                        mPlayer.setCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                v.setSelected(!v.isSelected());
//                            }
//                        });
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                });

    }
}
