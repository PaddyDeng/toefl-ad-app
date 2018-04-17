package io.dcloud.H58E83894.ui.make.everyDayListen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.callback.ICallBackImp;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.instestlisten.AllInstListenData;
import io.dcloud.H58E83894.data.make.AudioData;
import io.dcloud.H58E83894.data.make.ListenPracticeData;
import io.dcloud.H58E83894.data.make.PracticeQuestionData;
import io.dcloud.H58E83894.data.make.SentenceData;
import io.dcloud.H58E83894.excep.CustomException;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.common.TaskEndDialog;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.dcloud.H58E83894.weiget.LyricView;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class EveryListenPracticeActivity extends BaseActivity {

    public static void startEveryListenPractActivity(Context mContext, String id) {
        Intent intent = new Intent(mContext, EveryListenPracticeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, true);
        intent.putExtra(Intent.ACTION_ANSWER, id);
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
    @BindView(R.id.chinese_switch)
    ImageView chineseBt;
//    @BindView(R.id.next_question_fine_listen)
//    TextView nextQuestionFineListen;
    @BindView(R.id.relative)
    RelativeLayout relativeLayout;
    @BindView(R.id.relavite_01)
    RelativeLayout relativeLayout_01;
    @BindView(R.id.listen_name)
    TextView listenName;//标题
    @BindView(R.id.listen_time)
    TextView listTime;//时间

    private String url;
    private RxDownload mRxDownload;
    private MusicPlayer mPlayer;
    private boolean hasAdd;
    private List<SentenceData> mList;
    private TaskEndDialog mDialog;
    private boolean seeResult;
    private boolean isVisible = true;
    private String id;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        seeResult = intent.getBooleanExtra(Intent.EXTRA_TEXT, false);
        id = intent.getStringExtra(Intent.ACTION_ANSWER);
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
        setContentView(R.layout.activity_every_listen_article);

//        if (seeResult) {
//            nextQuestionFineListen.setBackgroundResource(R.drawable.com_btn_selector);
//            nextQuestionFineListen.setClickable(true);
//        }
    }

    @Override
    protected void asyncUiInfo() {
        super.asyncUiInfo();

        if(TextUtils.isEmpty(id) || id.equals("null")){return;}
        addToCompositeDis(HttpUtil
                .getQuListen(Integer.parseInt(id.trim()))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<AllInstListenData>() {
                    @Override
                    public void accept(@NonNull AllInstListenData data) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(data.getCode())) {
//                            refreshUi(data);
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

    @OnClick({R.id.listen_article_btn, R.id.chinese_switch, R.id.listen_article_menu,
            R.id.question_feed_back, R.id.listen_article_collect})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question_feed_back:
                showFeedBackDialog(id);
                break;

            case R.id.listen_article_menu://菜单
                forword(MyEveListenLessonActivity.class);
                break;

            case R.id.listen_article_collect://收藏
                if (hasAdd) {
                    if (mPlayer == null) return;
                    if (v.isSelected()) {
                        initCollect();
                    } else {
                        initCollect();
                    }
                }

                break;
//            case R.id.next_question_fine_listen://下一篇
//                nextQuestion();
//                break;
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

    private void initCollect() {

        addToCompositeDis(HttpUtil.getQuListenColl(1)//contentId
        .subscribe(new Consumer<AllInstListenData>() {
                    @Override
                    public void accept(@NonNull AllInstListenData bean) throws Exception {

//                        asyncSuccess(bean, isRefresh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        throwableDeal(throwable, isRefresh);
                    }
                }));

    }

    @Override
    protected void initView() {
        super.initView();

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVisible) {
                    relativeLayout.requestFocus();
                    chineseBt.setVisibility(View.GONE);
                    mLyricView.setVisibility(View.GONE);
                    relativeLayout_01.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                    isVisible = false;
                } else {
                    relativeLayout.clearFocus();
                    chineseBt.setVisibility(View.VISIBLE);
                    mLyricView.setVisibility(View.VISIBLE);
                    relativeLayout_01.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
        });

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

//    private void nextQuestion() {
//        addToCompositeDis(HttpUtil
//                .taskNext(C.FINE_LISTEN)
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        showLoadDialog();
//                    }
//                })
//                .flatMap(new Function<ResultBean, ObservableSource<ListenPracticeData>>() {
//                    @Override
//                    public ObservableSource<ListenPracticeData> apply(@NonNull ResultBean bean) throws Exception {
//                        mHandler.removeMessages(1);
//                        hasAdd = false;
//                        if (mPlayer.isPlaying()) {
//                            mPlayer.pause();
//                            mPlayer.seekTo(0);
//                        }
//                        fineListnBtn.setSelected(false);
//                        startTimeTv.setText(Utils.formatTime(0));
//                        mLyricView.setCurrentTimeMillis(0);
//                        if (getHttpResSuc(bean.getCode())) {
//                            RxBus.get().post(C.MAKE_FINE_LISTEN_REFRESH, C.MAKEING);
//                            return HttpUtil.listenPractice();
//                        } else if (bean.getCode() == 2) {
//                            RxBus.get().post(C.MAKE_FINE_LISTEN_REFRESH, C.MAKE_END);
//                            //做题完成
//                            throw new CustomException("test end");
//                        }
//                        return null;
//                    }
//                })
//                .subscribe(new Consumer<ListenPracticeData>() {
//                    @Override
//                    public void accept(@NonNull ListenPracticeData data) throws Exception {
//                        dismissLoadDialog();
//                        if (getHttpResSuc(data.getCode())) {
//                            refreshUi(data);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        dismissLoadDialog();
//                        if (throwable instanceof CustomException) {
//                            //弹出结束框
//                            if (mDialog == null) {
//                                mDialog = new TaskEndDialog();
//                            }
//                            mDialog.showDialog(getSupportFragmentManager());
//                        }
//                    }
//                }));
//    }

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

//    private void refreshUi(AllInstListenData data) {
//        if (data == null) return;
////        PracticeQuestionData questionData = data.getQuestion();
//        List<AllInstListenData.DataBean> bean = data.getData();
//        if (questionData == null) return;
//        mList = questionData.getSentence();
//        mLyricView.setLyricInfo(mList);
//        mLyricView.setOnPlayerClickListener(new LyricView.OnPlayerClickListener() {
//            @Override
//            public void onPlayerClicked(long progress, String content) {
//                mHandler.removeMessages(1);
//                if (mPlayer.isPlaying()) {
//                    mPlayer.pause();
//                }
//
//                mHandler.sendEmptyMessage(1);
//                if (hasAdd) {
//                    mPlayer.seekTo((int) progress);
//                    mPlayer.resume();
//                    fineListnBtn.setSelected(true);
//                } else {
//                    play(fineListnBtn, (int) progress);
//                }
//            }
//        });
//        AudioData audio = questionData.getAudio();
//        if (audio == null) return;
//        id = audio.getId();
//        url = RetrofitProvider.TOEFLURL + audio.getFilePath();
//        DownloadUtil.download(url, mRxDownload, mRxPermissions, new RequestImp<DownloadStatus>() {
//            @Override
//            public void requestSuccess(DownloadStatus downloadStatus) {
//                if (downloadStatus.getPercentNumber() == 100) {
//                    File[] record = mRxDownload.getRealFiles(url);
//                    if (record[0].exists()) {
//                        mHandler.sendEmptyMessageDelayed(1, 100);
//                        long time = MediaUtil.getMediaTime(record[0].getAbsolutePath());
//                        allTimeTv.setText(Utils.formatTime(time));
//                        seekBar.setMax((int) time);
//                        seekBar.setProgress(0);
//                    }
//                }
//            }
//        });
//    }
}
