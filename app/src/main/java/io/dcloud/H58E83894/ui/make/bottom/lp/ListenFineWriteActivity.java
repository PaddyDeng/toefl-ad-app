package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import io.dcloud.H58E83894.base.adapter.DividerGridItemDecoration;
import io.dcloud.H58E83894.callback.ICallBackImp;
import io.dcloud.H58E83894.data.MediaData;
import io.dcloud.H58E83894.data.make.AudioData;
import io.dcloud.H58E83894.data.make.ListenWriteGridData;
import io.dcloud.H58E83894.data.make.PracticeQuestionData;
import io.dcloud.H58E83894.data.make.SentenceData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.make.adapter.ListenWriteAdapter;
import io.dcloud.H58E83894.utils.DownloadUtil;
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
import zlc.season.rxdownload2.entity.DownloadStatus;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ListenFineWriteActivity extends BaseActivity {

    private ListenWriteAdapter mAdapter;
    private List<SentenceData> mSentence;
    private boolean hasAdd;

    public static void startListenWriteAct(Context c, PracticeQuestionData data, String title, int index) {
        Intent intent = new Intent(c, ListenFineWriteActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        c.startActivity(intent);
    }

    @BindView(R.id.fine_listen_wirte_title)
    TextView titleTv;
    @BindView(R.id.wirte_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.write_question_tv)
    TextView questionTv;
    @BindView(R.id.listen_line_title_tv)
    TextView questionSerialTv;
    @BindView(R.id.eye_status_iv)
    ImageView eyeImg;
    @BindView(R.id.looper_img)
    ImageView loopImg;
    @BindView(R.id.fine_listen_control_img)
    ImageView controlImg;
    private PracticeQuestionData mPracticeQuestionData;
    private int index;
    private String url;
    private RxDownload mRxDownload;
    private MusicPlayer mPlayer;
    private int startTime;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (hasAdd) {
                int position = mPlayer.getCurrPosition();
                int cacl = cacl(position);
                if (loopImg.isSelected()) {//需要循环，否则不需要循环
                    SentenceData data = mSentence.get(index);
                    startTime = (int) (Double.valueOf(data.getStart_time()) * 1000);
                    int endTime = startTime + (int) (Double.valueOf(data.getAudio_time()) * 1000);
                    if (position >= endTime || position < startTime) {
                        mPlayer.pause();
                        mPlayer.seekTo(startTime);
                        mPlayer.resume();
                    }
                } else if (index != cacl) {
                    index = cacl;
                    initPageData();
                }
            }
            mHandler.sendEmptyMessageDelayed(1, 100);
            return false;
        }
    });


    private int cacl(int position) {
        for (SentenceData sd : mSentence) {
            int startTime = (int) (Double.valueOf(sd.getStart_time()) * 1000);
            int endTime = startTime + (int) (Double.valueOf(sd.getAudio_time()) * 1000);
            if (position >= startTime && position < endTime) {
                return mSentence.indexOf(sd);
            }
        }
        return 0;
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        mPracticeQuestionData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        index = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
        titleTv.setText(intent.getStringExtra(Intent.EXTRA_TITLE));
    }

    @Override
    protected void initView() {
        super.initView();
        initRecycler(mRecyclerView, new GridLayoutManager(mContext, 3));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(mContext, R.drawable.whitle_grid_item_divider));
        if (mPracticeQuestionData == null) return;
        initPageData();
        AudioData audio = mPracticeQuestionData.getAudio();
        initMedia(audio.getFilePath());
    }

    private void initPageData() {
        mSentence = mPracticeQuestionData.getSentence();
        questionSerialTv.setText(getString(R.string.str_listen_topic_hint_info, index + 1, mSentence.size()));
        SentenceData data = mSentence.get(index);
        String dataContent = data.getContent();
        questionTv.setText(dataContent);
        List<String> singleQuestionList = Utils.splitStr(dataContent, " ");
        initDataRecycler(singleQuestionList);
    }

    private void initMedia(String filePath) {
        url = RetrofitProvider.TOEFLURL + filePath;
        DownloadUtil.download(url, mRxDownload, mRxPermissions, new RequestImp<DownloadStatus>() {
            @Override
            public void requestSuccess(DownloadStatus downloadStatus) {
                if (downloadStatus.getPercentNumber() == 100) {
                    File[] record = mRxDownload.getRealFiles(url);
                    if (record[0].exists()) {
                        mHandler.sendEmptyMessageDelayed(1, 100);
                    }
                }
            }
        });
    }

    private void initDataRecycler(List<String> list) {
        List<ListenWriteGridData> writeDatas = new ArrayList<>();
        for (String qu : list) {
            ListenWriteGridData data = new ListenWriteGridData();
            data.setCorrectAnswer(qu);
            writeDatas.add(data);
        }
        if (mAdapter == null) {
            mAdapter = new ListenWriteAdapter(writeDatas);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.update(writeDatas);
        }
        Utils.keyBordHideFromWindow(mContext, questionTv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxDownload = RxDownload.getInstance(mContext);
        mPlayer = new MusicPlayer();
        setContentView(R.layout.activity_listen_fine_write);
    }

    @OnClick({R.id.eye_status_container, R.id.media_looper_container, R.id.fine_listen_control_img,
            R.id.left_write_img, R.id.right_write_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_write_img:
                jumpMedia(false);
                break;
            case R.id.left_write_img:
                jumpMedia(true);
                break;
            case R.id.eye_status_container:
                switchStatus(eyeImg);
                break;
            case R.id.media_looper_container:
                changeLooper(loopImg);
                break;
            case R.id.fine_listen_control_img:
                play(v);
            default:
                break;
        }
    }

    private void jumpMedia(boolean left) {
        if (loopImg.isSelected()) return;//是循环播放，不用播放上一句或下一句
        if (left) {
            index = index > 0 ? --index : 0;
        } else {
            int lowSize = mSentence.size() - 1;
            index = index < lowSize ? ++index : lowSize;
        }
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        if (hasAdd) {
            mPlayer.seekTo((int) (Double.valueOf(mSentence.get(index).getStart_time()) * 1000));
            mPlayer.resume();
            controlImg.setSelected(true);
        } else {
            play(controlImg);
        }
        initPageData();
    }

    private void play(final View v) {
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

        SentenceData sentenceData = mSentence.get(index);
        startTime = (int) (Double.valueOf(sentenceData.getStart_time()) * 1000);
        mPlayer.play(mRxPermissions, mRxDownload, url,
                startTime, v, new ICallBackImp() {
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
//                        SentenceData sentenceData = mSentence.get(index);
//                        startTime = (int) (Double.valueOf(sentenceData.getStart_time()) * 1000);
//                        mPlayer.setQueue(list, list.size() - 1, startTime);
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

    private void changeLooper(ImageView img) {
        img.setSelected(!img.isSelected());
    }

    private void switchStatus(View v) {
        v.setSelected(!v.isSelected());
        int visibility = questionTv.getVisibility();
        if (visibility == View.GONE) {
            Utils.setVisible(questionTv);
        } else if (visibility == View.VISIBLE) {
            Utils.setGone(questionTv);
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
}
