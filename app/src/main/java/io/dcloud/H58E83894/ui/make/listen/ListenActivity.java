package io.dcloud.H58E83894.ui.make.listen;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.callback.ICallBackImp;
import io.dcloud.H58E83894.data.MediaData;
import io.dcloud.H58E83894.data.make.ListenData;
import io.dcloud.H58E83894.data.make.ListeningBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.dcloud.H58E83894.weiget.CirclePlayView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;
import zlc.season.rxdownload2.entity.DownloadStatus;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static io.dcloud.H58E83894.utils.Utils.formatTime;

public class ListenActivity extends BaseActivity {

    @BindView(R.id.listen_des_tv)
    TextView desTxt;
    @BindView(R.id.listen_time_tv)
    TextView listenTime;
    @BindView(R.id.circle_play_view)
    CirclePlayView circlePlayView;
    @BindView(R.id.listen_play_iv)
    ImageView mImageView;

    private RxDownload mRxDownload;
    private MusicPlayer mPlayer;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mPlayer != null && !down) {
                int position = mPlayer.getCurrPosition();
                setInfo(position);
            }
            mHandler.sendEmptyMessageDelayed(1, 100);
            return false;
        }
    });

    private void setInfo(int position) {
        listenTime.setText(getString(R.string.str_listen_time, formatTime(position), formatTime(max)));
        circlePlayView.setProgress(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxDownload = RxDownload.getInstance(mContext);
        mPlayer = new MusicPlayer();
        setContentView(R.layout.activity_listen);
    }

    private int mProgress = -1;
    private boolean down;

    @Override
    protected void initData() {
        circlePlayView.setOnTimeChangedListener(new CirclePlayView.OnTimeChangedListener() {
            @Override
            public void progress(int progress) {
                mProgress = progress;
            }

            @Override
            public void down() {
                down = true;
                if (mPlayer != null) {
                    mPlayer.pause();
                }
                if (mImageView.isSelected())
                    mImageView.setSelected(false);
            }

            @Override
            public void up() {
                down = false;
                switchPlay(mImageView);
            }
        });
    }

    @OnClick({R.id.listen_start_btn, R.id.listen_play_iv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listen_start_btn:
                if (mPlayer != null) {
                    mPlayer.stop();
                    if (mImageView.isSelected())
                        mImageView.setSelected(!mImageView.isSelected());
                }
                forword(ListenTestActivity.class);
                break;
            case R.id.listen_play_iv:
                switchPlay(v);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private boolean hasAdd = false;

    private void setProgress() {
        if (mProgress != -1) {
            mPlayer.seekTo(mProgress);
            mProgress = -1;
        }
    }

    private void switchPlay(final View v) {
        v.setSelected(!v.isSelected());
        if (hasAdd) {
            if (mPlayer == null) return;
            setProgress();
            if (v.isSelected()) {
                mPlayer.resume();
            } else {
                mPlayer.pause();
            }
            return;
        }
        mPlayer.play(mRxPermissions, mRxDownload, url, mProgress, v,
                new ICallBackImp() {
                    @Override
                    public void onSuccess(Object o) {
                        hasAdd = true;
                    }
                });

//
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
//                        mPlayer.setQueue(list, list.size() - 1, mProgress);
//                        mPlayer.setCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                v.setSelected(!v.isSelected());
//                            }
//                        });
//                        hasAdd = true;
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                });
    }

    @Override
    protected void asyncUiInfo() {
        super.asyncUiInfo();
        addToCompositeDis(HttpUtil
                .listen()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ListenData>() {
                    @Override
                    public void accept(@NonNull ListenData data) throws Exception {
                        if (getHttpResSuc(data.getCode())) {
                            refreshUi(data);
                        } else {
                            asyncUiInfo();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi(ListenData data) {
        ListeningBean listening = data.getIntensiveListening();
        if (listening == null) return;
        desTxt.setText(listening.getName());
        download(RetrofitProvider.TOEFLURL + listening.getAnswer());

    }

    private String url;
    private int max;

    private void download(final String url) {
        this.url = url;
        DownloadUtil.download(url, mRxDownload, mRxPermissions, new RequestImp<DownloadStatus>() {
            @Override
            public void requestSuccess(DownloadStatus status) {
                if (status.getPercentNumber() == 100) {
                    dismissLoadDialog();
                    File[] record = mRxDownload.getRealFiles(url);
                    if (record[0].exists()) {
                        mHandler.sendEmptyMessageDelayed(1, 100);
                        long time = MediaUtil.getMediaTime(record[0].getAbsolutePath());
                        max = (int) time;
                        circlePlayView.setMax(max);
                        circlePlayView.setProgress(0);
                        listenTime.setText(getString(R.string.str_listen_time, formatTime(0), formatTime(time)));
                    }
                }
            }

            @Override
            public void requestFail(String msg) {
                dismissLoadDialog();
                toastShort(msg);
            }
        });
    }
}
