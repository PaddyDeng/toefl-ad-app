package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import io.dcloud.H58E83894.data.make.ListenQuestionData;
import io.dcloud.H58E83894.data.make.ListenTpoContentData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.dcloud.H58E83894.weiget.CircleImageView;
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

public class ListenSecQuestionActivity extends BaseActivity {

    public static void startListenSecQuestionAct(Context c, String id, String title) {
        Intent intent = new Intent(c, ListenSecQuestionActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        c.startActivity(intent);
    }

    @BindView(R.id.listen_des_tv)
    TextView desTxt;
    @BindView(R.id.listen_time_tv)
    TextView listenTime;
    @BindView(R.id.circle_play_view)
    CirclePlayView circlePlayView;
    @BindView(R.id.listen_play_iv)
    ImageView mImageView;
    @BindView(R.id.list_center_txt)
    TextView titleTv;
    @BindView(R.id.circle_bg_iv)
    CircleImageView mCircleImageView;

    private RxDownload mRxDownload;
    private MusicPlayer mPlayer;
    private String id;
    private int mProgress = -1;
    private boolean down;
    private String url;
    private int max;
    private Observable<String> busObs;

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
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_INDEX);
        String title = intent.getStringExtra(Intent.EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxDownload = RxDownload.getInstance(mContext);
        mPlayer = new MusicPlayer();
        setContentView(R.layout.activity_listen_sec_question);
    }

    @Override
    protected void initView() {
        super.initView();
        busObs = RxBus.get().register(C.REFRESH_LISTEN_LIST, String.class);
        busObs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                if (TextUtils.equals(s, id)) {
                    finish();
                }
            }
        });
    }

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
                if (TextUtils.isEmpty(id)) return;
                if (mPlayer != null) {
                    mPlayer.stop();
                    if (mImageView.isSelected())
                        mImageView.setSelected(!mImageView.isSelected());
                }
                ListenSecTestActivity.startSecTestAct(mContext, id);
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
        if (busObs != null) {
            RxBus.get().unregister(C.REFRESH_LISTEN_LIST, busObs);
        }
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
        mPlayer.play(mRxPermissions, mRxDownload, url,
                mProgress, v, new ICallBackImp() {
                    @Override
                    public void onSuccess(Object o) {
                        hasAdd = true;
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
//                        list = new ArrayList<MediaData>();
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

        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil
                .listenTopicRequest(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ListenQuestionData>() {
                    @Override
                    public void accept(@NonNull ListenQuestionData data) throws Exception {
                        dismissLoadDialog();
                        refreshUi(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi(ListenQuestionData data) {
        if (data == null) return;
        ListenTpoContentData currentData = data.getCurrentData();
        if (currentData == null) return;
        GlideUtil.load(RetrofitProvider.TOEFLURL + currentData.getImage(), mCircleImageView);
        desTxt.setText(currentData.getName());
        download(RetrofitProvider.TOEFLURL + currentData.getFile());

    }

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
