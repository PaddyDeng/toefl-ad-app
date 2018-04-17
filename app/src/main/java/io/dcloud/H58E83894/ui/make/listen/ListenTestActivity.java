package io.dcloud.H58E83894.ui.make.listen;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import io.dcloud.H58E83894.data.make.ListenData;
import io.dcloud.H58E83894.data.make.QuestionBean;
import io.dcloud.H58E83894.excep.CustomException;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.common.TaskEndDialog;
import io.dcloud.H58E83894.ui.make.grammar.GrammarTestActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.dcloud.H58E83894.weiget.RealtimeBlurView;
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

public class ListenTestActivity extends BaseActivity {

    public static void startListenTestActivity(Context mContext) {
        Intent intent = new Intent(mContext, ListenTestActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, true);
        mContext.startActivity(intent);
    }

    @BindView(R.id.listen_en_tv)
    TextView enTxt;
    @BindView(R.id.listen_ch_tv)
    TextView chTxt;
    @BindView(R.id.listen_next_btn)
    TextView bthTxt;
    @BindView(R.id.ch_container)
    RelativeLayout chContainer;
    @BindView(R.id.ch_blur)
    RealtimeBlurView chBlur;
    @BindView(R.id.en_container)
    RelativeLayout enContainer;
    @BindView(R.id.en_blur)
    RealtimeBlurView enBlur;
    @BindView(R.id.listen_audio_time_tv)
    TextView timeTxt;
    @BindView(R.id.listen_test_seekbar)
    SeekBar seekBar;
    @BindView(R.id.listen_test_status_img)
    ImageView statusImg;

    private TaskEndDialog mDialog;
    private String[] options = new String[]{"A", "B", "C", "D", "E", "F"};
    private RxDownload mRxDownload;
    private boolean downEnd;
    private String url;
    private String recoreUrl;
    private MusicPlayer mPlayer;
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
            if (mPlayer != null && TextUtils.equals(recoreUrl, url)) {
                int position = mPlayer.getCurrPosition();
                seekBar.setProgress(position);
                timeTxt.setText(Utils.formatTime(position));
            }
            mHandler.sendEmptyMessageDelayed(1, 100);
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxDownload = RxDownload.getInstance(mContext);
        setContentView(R.layout.activity_listen_test);
        if (seeResult) {
            bthTxt.setBackgroundResource(R.drawable.com_btn_selector);
            bthTxt.setClickable(true);
        }
        mPlayer = new MusicPlayer();
        mHandler.sendEmptyMessageDelayed(1, 100);
    }

    @Override
    protected void initView() {
        super.initView();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    statusImg.setSelected(statusImg.isSelected());
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mPlayer != null && downEnd) {
                    //获取当前拖拽的位置
                    int dragedProgress = seekBar.getProgress();
                    //记录拖拽的位置
                    mPlayer.seekTo(dragedProgress);
                    timeTxt.setText(Utils.formatTime(dragedProgress));
                    mPlayer.resume();
                    statusImg.setSelected(true);
                }
            }
        });
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
                            dismissLoadDialog();
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
        if (data == null) return;
        Utils.setVisible(chBlur, enBlur);
        QuestionBean question = data.getQuestion();
        if (question == null) return;
        id = question.getId();
        downEnd = false;
        String[] str = Utils.splitOption(question.getAlternatives());
        String answer = question.getAnswer();
        int index = 0;
        for (String opt : options) {
            if (TextUtils.equals(answer, opt)) {
                break;
            }
            index++;
        }
        chTxt.setText(str[index].replace(answer, "").toString());
        addOnGlobalListener(chTxt, chBlur);

        enTxt.setText(question.getListeningFile());
        addOnGlobalListener(enTxt, enBlur);

        url = RetrofitProvider.TOEFLURL + question.getCnName();

        DownloadUtil.download(url, mRxDownload, mRxPermissions, new RequestImp<DownloadStatus>() {
            @Override
            public void requestSuccess(DownloadStatus status) {
                if (status.getPercentNumber() == 100) {
                    downEnd = true;
                    File[] record = mRxDownload.getRealFiles(url);
                    if (record[0].exists()) {
                        long time = MediaUtil.getMediaTime(record[0].getAbsolutePath());
                        seekBar.setMax((int) time);
                        seekBar.setProgress(0);
                    }
                }
            }

            @Override
            public void requestFail(String msg) {
                toastShort(msg);
            }
        });
    }

    private void addOnGlobalListener(final TextView txt, final RealtimeBlurView rbv) {
        txt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(txt,this);
                rbv.getLayoutParams().height = txt.getMeasuredHeight();
                rbv.requestLayout();
            }
        });
    }

    @OnClick({R.id.ch_container, R.id.en_container, R.id.listen_next_btn, R.id.listen_test_status_img,
            R.id.question_feed_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question_feed_back:
                showFeedBackDialog(id);
                break;
            case R.id.listen_test_status_img:
                if (!downEnd) {
                    toastShort(R.string.str_src_downloading);
                    return;
                }
                switchImgStatus(v);
                break;
            case R.id.ch_container:
                showOrHide(chBlur);
                break;
            case R.id.en_container:
                showOrHide(enBlur);
                break;
            case R.id.listen_next_btn:
                nextQuestion();
                break;
            default:
                break;
        }
    }

    private void switchImgStatus(final View v) {
        v.setSelected(!v.isSelected());
        if (!TextUtils.equals(recoreUrl, url)) {
            recoreUrl = url;
            play(v);
        } else {
            if (mPlayer == null) return;
            if (v.isSelected()) {
                mPlayer.resume();
            } else {
                mPlayer.pause();
            }
        }
    }

    private void play(final View v) {
        mPlayer.play(mRxPermissions, mRxDownload, url, 0, v, null);

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

    private void nextQuestion() {
        addToCompositeDis(HttpUtil.taskNext(C.LISTEN)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .flatMap(new Function<ResultBean, ObservableSource<ListenData>>() {
                    @Override
                    public ObservableSource<ListenData> apply(@NonNull ResultBean bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            RxBus.get().post(C.MAKE_LISTEN_REFRESH, C.MAKEING);
                            return HttpUtil.listen();
                        } else if (bean.getCode() == 2) {
                            //做题完成
                            RxBus.get().post(C.MAKE_LISTEN_REFRESH, C.MAKE_END);
                            throw new CustomException("test end");
                        }
                        return null;
                    }
                })
                .subscribe(new Consumer<ListenData>() {
                    @Override
                    public void accept(@NonNull ListenData data) throws Exception {
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

    private void showOrHide(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            Utils.setGone(view);
        } else {
            Utils.setVisible(view);
        }
    }

}
