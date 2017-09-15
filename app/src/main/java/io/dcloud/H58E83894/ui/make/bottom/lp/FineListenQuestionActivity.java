package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import io.dcloud.H58E83894.data.make.AudioData;
import io.dcloud.H58E83894.data.make.PracticeQuestionData;
import io.dcloud.H58E83894.data.make.SentenceData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.dcloud.H58E83894.weiget.LyricView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class FineListenQuestionActivity extends BaseActivity {

    public static void startFineQuestionAct(Context c, String id, String title) {
        Intent intent = new Intent(c, FineListenQuestionActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        c.startActivity(intent);
    }

    @BindView(R.id.fine_listen_question_title)
    TextView titleTv;
    @BindView(R.id.lyric_view_Container)
    RelativeLayout lyricViewContainer;
    @BindView(R.id.chinese_switch)
    ImageView switchLyricImg;
    @BindView(R.id.lyric_view)
    LyricView mLyricView;
    @BindView(R.id.media_all_time)
    TextView allTimeTv;
    @BindView(R.id.media_start_tv)
    TextView startTimeTv;
    @BindView(R.id.media_seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.fine_listen_contr_btn)
    ImageView fineListnBtn;
    @BindView(R.id.fine_listen_line_title_tv)
    TextView singleTitleTv;
    @BindView(R.id.single_title_en_des)
    TextView singleEnTv;
    @BindView(R.id.single_chinese_tv)
    TextView singleChineseTv;
    @BindView(R.id.show_type_tv)
    TextView showTypeTv;
    @BindView(R.id.single_container)
    LinearLayout singleContainer;

    private String id;
    private PracticeQuestionData mPracticeQuestionData;
    private String title;
    private String url;
    private RxDownload mRxDownload;
    private MusicPlayer mPlayer;
    private List<SentenceData> mSentence;
    private boolean hasAdd;
    private int recordIndex;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mPlayer != null) {
                int position = mPlayer.getCurrPosition();
                int oldIndex = recordIndex;
                cacl(position);
                mLyricView.setCurrentTimeMillis(position);
                startTimeTv.setText(Utils.formatTime(position));
                mSeekBar.setProgress(position);
                if (oldIndex != recordIndex) {
                    refreshSingleUi();
                }
            }
            mHandler.sendEmptyMessageDelayed(1, 100);
            return false;
        }
    });

    private void refreshSingleUi() {
        SentenceData data = mSentence.get(recordIndex);
        singleTitleTv.setText(getString(R.string.str_listen_topic_hint_info, recordIndex + 1, mSentence.size()));
        singleEnTv.setText(data.getContent());
        singleChineseTv.setText(data.getCnSentence());
        switchChineseStatus();
    }

    private void switchChineseStatus() {
        if (switchLyricImg.isSelected()) {
            Utils.setVisible(singleChineseTv);
        } else {
            Utils.setGone(singleChineseTv);
        }
    }

    private void cacl(int position) {
        if (mSentence == null || mSentence.isEmpty()) return;
        for (SentenceData sd : mSentence) {
            String start_time = sd.getStart_time();
            String audio_time = sd.getAudio_time();
            if (start_time.contains("。")) {
                start_time = start_time.replace("。", ".");
            }
            if (audio_time.contains("。")) {
                audio_time = audio_time.replace("。", ".");
            }
            int startTime = (int) (Double.valueOf(start_time) * 1000);
            int endTime = startTime + (int) (Double.valueOf(audio_time) * 1000);
            if (position >= startTime && position < endTime) {
                recordIndex = mSentence.indexOf(sd);
                break;
            }
        }
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_INDEX);
        title = intent.getStringExtra(Intent.EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = new MusicPlayer();
        mRxDownload = RxDownload.getInstance(mContext);
        setContentView(R.layout.activity_fine_listen_question);
    }

    @OnClick({R.id.listen_fine_write_tv, R.id.click_screen_show_original, R.id.fine_listen_contr_btn,
            R.id.chinese_switch, R.id.left_go_iv, R.id.right_go_iv, R.id.swithc_show_content})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_go_iv:
                leftOrRightGo(true);
                break;
            case R.id.right_go_iv:
                leftOrRightGo(false);
                break;
            case R.id.listen_fine_write_tv:
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                }
                fineListnBtn.setSelected(false);
                if (mPracticeQuestionData == null) return;
                ListenFineWriteActivity.startListenWriteAct(mContext, mPracticeQuestionData, title, recordIndex);
                break;
            case R.id.click_screen_show_original:
                Utils.setGone(v);
                Utils.setVisible(lyricViewContainer);
                break;
            case R.id.fine_listen_contr_btn:
                play(v);
                break;
            case R.id.chinese_switch:
                mLyricView.switchVisiable(v);
                switchChineseStatus();
                break;
            case R.id.swithc_show_content:
//                showHint
                String type = showTypeTv.getText().toString();
                String fullText = getString(R.string.str_full_text);
                if (TextUtils.equals(type, fullText)) {
                    Utils.setGone(singleContainer);
                    Utils.setVisible(mLyricView);
                    showTypeTv.setText(getString(R.string.str_single));
                } else {
                    Utils.setGone(mLyricView);
                    Utils.setVisible(singleContainer);
                    showTypeTv.setText(fullText);
                }
                break;
            default:
                break;
        }
    }

    private void leftOrRightGo(boolean toLeft) {
        if (mSentence == null || mSentence.isEmpty()) return;
        if (toLeft) {
            recordIndex = recordIndex > 0 ? --recordIndex : 0;
        } else {
            int lowSize = mSentence.size() - 1;
            recordIndex = recordIndex < lowSize ? ++recordIndex : lowSize;
        }
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        if (hasAdd) {
            mPlayer.seekTo((int) (Double.valueOf(mSentence.get(recordIndex).getStart_time()) * 1000));
            mPlayer.resume();
            fineListnBtn.setSelected(true);
        } else {
            play(fineListnBtn);
        }
    }


    @Override
    protected void initView() {
        super.initView();
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        mPlayer.play(mRxPermissions, mRxDownload, url, progress, v,
                new ICallBackImp() {
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
    protected void asyncUiInfo() {
        super.asyncUiInfo();
        addToCompositeDis(HttpUtil
                .fineListen(id)
                .subscribe(new Consumer<PracticeQuestionData>() {
                    @Override
                    public void accept(@NonNull PracticeQuestionData data) throws Exception {
                        mPracticeQuestionData = data;
                        refreshUi(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi(PracticeQuestionData data) {
        if (data == null) return;
        mSentence = data.getSentence();
        mLyricView.setLyricInfo(mSentence);
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
        refreshSingleUi();
        AudioData audio = data.getAudio();
        if (audio == null) return;
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
                        mSeekBar.setMax((int) time);
                        mSeekBar.setProgress(0);
                    }
                }
            }
        });
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

}
