package io.dcloud.H58E83894.ui.make.practice;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.AudioData;
import io.dcloud.H58E83894.data.make.ListenPracticeData;
import io.dcloud.H58E83894.data.make.PracticeQuestionData;
import io.dcloud.H58E83894.data.make.SentenceData;
import io.dcloud.H58E83894.excep.CustomException;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.common.TaskEndDialog;
import io.dcloud.H58E83894.ui.make.listen.ListenTestActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.dcloud.H58E83894.weiget.LyricView;
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

public class ListenPracticeActivity extends BaseActivity {

    public static void startListenPraTestActivity(Context mContext) {
        Intent intent = new Intent(mContext, ListenPracticeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, true);
        mContext.startActivity(intent);
    }

    @BindView(R.id.lyric_view)
    LyricView mLyricView;
    @BindView(R.id.end_time)
    TextView allTimeTv;
    @BindView(R.id.start_time)
    TextView startTimeTv;
    @BindView(R.id.music_seek_bar)
    SeekBar seekBar;
    @BindView(R.id.listen_article_btn)
    ImageView fineListnBtn;
    @BindView(R.id.next_question_fine_listen)
    TextView nextQuestionFineListen;
    private String url;
    private RxDownload mRxDownload;
    private MusicPlayer mPlayer;
    private boolean hasAdd;
    private List<SentenceData> mList;
    private TaskEndDialog mDialog;
    private String id;
    private boolean seeResult;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        seeResult = intent.getBooleanExtra(Intent.EXTRA_TEXT, false);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mPlayer != null) {
                int position = mPlayer.getCurrPosition();
                mLyricView.setCurrentTimeMillis(position);
                startTimeTv.setText(Utils.formatTime(position));
                seekBar.setProgress(position);
            }
            mHandler.sendEmptyMessageDelayed(1, 100);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxDownload = RxDownload.getInstance(mContext);
        mPlayer = new MusicPlayer();
        setContentView(R.layout.activity_listen_article);

        if (seeResult) {
            nextQuestionFineListen.setBackgroundResource(R.drawable.com_btn_selector);
            nextQuestionFineListen.setClickable(true);
        }
    }

    @Override
    protected void asyncUiInfo() {
        super.asyncUiInfo();
        addToCompositeDis(HttpUtil
                .listenPractice()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ListenPracticeData>() {
                    @Override
                    public void accept(@NonNull ListenPracticeData data) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(data.getCode())) {
                            refreshUi(data);
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

    @OnClick({R.id.listen_article_btn, R.id.chinese_switch, R.id.next_question_fine_listen,
            R.id.question_feed_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question_feed_back:
                showFeedBackDialog(id);
            case R.id.next_question_fine_listen://下一篇
                nextQuestion();
                break;
            case R.id.listen_article_btn:
                play(v);
                break;
            case R.id.chinese_switch:
                mLyricView.switchVisiable(v);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (hasAdd) {
                        mPlayer.seekTo(progress);
                    } else {
                        play(fineListnBtn, progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeMessages(1);
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.sendEmptyMessage(1);
                if (hasAdd) {
                    mPlayer.resume();
                    fineListnBtn.setSelected(true);
                } else {
                    play(fineListnBtn);
                }
            }
        });
    }

    private void nextQuestion() {
        addToCompositeDis(HttpUtil
                .taskNext(C.FINE_LISTEN)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .flatMap(new Function<ResultBean, ObservableSource<ListenPracticeData>>() {
                    @Override
                    public ObservableSource<ListenPracticeData> apply(@NonNull ResultBean bean) throws Exception {
                        mHandler.removeMessages(1);
                        hasAdd = false;
                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                            mPlayer.seekTo(0);
                        }
                        fineListnBtn.setSelected(false);
                        startTimeTv.setText(Utils.formatTime(0));
                        mLyricView.setCurrentTimeMillis(0);
                        if (getHttpResSuc(bean.getCode())) {
                            RxBus.get().post(C.MAKE_FINE_LISTEN_REFRESH, C.MAKEING);
                            return HttpUtil.listenPractice();
                        } else if (bean.getCode() == 2) {
                            RxBus.get().post(C.MAKE_FINE_LISTEN_REFRESH, C.MAKE_END);
                            //做题完成
                            throw new CustomException("test end");
                        }
                        return null;
                    }
                })
                .subscribe(new Consumer<ListenPracticeData>() {
                    @Override
                    public void accept(@NonNull ListenPracticeData data) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(data.getCode())) {
                            refreshUi(data);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        if (throwable instanceof CustomException) {
                            //弹出结束框
                            if (mDialog == null) {
                                mDialog = new TaskEndDialog();
                            }
                            mDialog.showDialog(getSupportFragmentManager());
                        }
                    }
                }));
    }

    private void play(final View v, final int progress) {
        v.setSelected(!v.isSelected());
        if (hasAdd) {
            if (mPlayer == null) return;
            if (v.isSelected()) {
                mPlayer.resume();
            } else {
                mPlayer.pause();
            }
            return;
        }
        mPlayer.play(mRxPermissions, mRxDownload, url,
                progress, v, new ICallBackImp() {
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
//                        List<MediaData> list = new ArrayList<MediaData>();
//                        MediaData data = new MediaData();
//                        data.setName(record.getSaveName());
//                        data.setPath(TextUtils.concat(record.getSavePath(), File.separator, record.getSaveName()).toString());
//                        list.add(data);
//                        mPlayer.setQueue(list, list.size() - 1, progress);
//
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
//                        v.setSelected(!v.isSelected());
//                    }
//                });
    }

    private void play(View v) {
        play(v, 0);
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

    private void refreshUi(ListenPracticeData data) {
        if (data == null) return;
        PracticeQuestionData questionData = data.getQuestion();
        if (questionData == null) return;
        mList = questionData.getSentence();
        mLyricView.setLyricInfo(mList);
        Log.i("mLyricView", mLyricView +" =" +mList.toString());
        mLyricView.setOnPlayerClickListener(new LyricView.OnPlayerClickListener() {
            @Override
            public void onPlayerClicked(long progress, String content) {
                mHandler.removeMessages(1);
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                }

                mHandler.sendEmptyMessage(1);
                if (hasAdd) {
                    mPlayer.seekTo((int) progress);
                    mPlayer.resume();
                    fineListnBtn.setSelected(true);
                } else {
                    play(fineListnBtn, (int) progress);
                }
            }
        });
        AudioData audio = questionData.getAudio();
        if (audio == null) return;
        id = audio.getId();
        url = RetrofitProvider.TOEFLURL + audio.getFilePath();
        DownloadUtil.download(url, mRxDownload, mRxPermissions, new RequestImp<DownloadStatus>() {
            @Override
            public void requestSuccess(DownloadStatus downloadStatus) {
                if (downloadStatus.getPercentNumber() == 100) {
                    File[] record = mRxDownload.getRealFiles(url);
                    if (record[0].exists()) {
                        mHandler.sendEmptyMessageDelayed(1, 100);
                        long time = MediaUtil.getMediaTime(record[0].getAbsolutePath());
                        allTimeTv.setText(Utils.formatTime(time));
                        seekBar.setMax((int) time);
                        seekBar.setProgress(0);
                    }
                }
            }
        });
    }
}
