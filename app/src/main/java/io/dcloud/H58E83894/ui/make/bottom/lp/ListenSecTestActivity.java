package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.callback.ICallBackImp;
import io.dcloud.H58E83894.data.MediaData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.AnswerData;
import io.dcloud.H58E83894.data.make.ListenChildData;
import io.dcloud.H58E83894.data.make.ListenQuestionData;
import io.dcloud.H58E83894.data.make.ListenSecRecordData;
import io.dcloud.H58E83894.data.make.ListenTpoContentData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.make.adapter.ListenSecAnswerAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ListenSecTestActivity extends BaseActivity {

    public static void startSecTestAct(Context c, String id) {
        Intent intent = new Intent(c, ListenSecTestActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        c.startActivity(intent);
    }

    @BindView(R.id.listen_sec_question_tv)
    TextView questionTv;
    @BindView(R.id.answer_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.commit_answer_goon_next_btn)
    TextView nextBtn;
    @BindView(R.id.test_listen_title)
    TextView titleTv;
    @BindView(R.id.listen_sec_test_img)
    ImageView listenImg;
    private String id;
    private int index = 0;//默认第一题开始
    private List<ListenChildData> child;
    private String[] options = new String[]{"A", "B", "C", "D", "E", "F"};
    private List<AnswerData> mDataList;
    private ListenSecAnswerAdapter mAdapter;
    private String userAnswer;
    private String pid;
    private boolean isMultChoose;
    private int answerNum;
    private List<String> answerData;
    private String url;
    private MusicPlayer player;
    private RxDownload rxDownload;
    private int type;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_INDEX);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxDownload = RxDownload.getInstance(mContext);
        setContentView(R.layout.activity_listen_sec_test);
        setNextClickAble(false);
    }

    @Override
    protected void initView() {
        initRecycler(mRecyclerView, new LinearLayoutManager(this));
    }

    private void setNextClickAble(boolean clickable) {
        nextBtn.setClickable(clickable);
        if (clickable) {
            nextBtn.setBackgroundResource(R.drawable.com_btn_selector);
        } else {
            nextBtn.setBackgroundResource(R.drawable.no_clickable_bg);
        }
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
        child = currentData.getChild();
        List<ListenSecRecordData> record = currentData.getRecord();
        if (record == null || record.isEmpty()) {
            index = 0;
        } else {
            index = record.size();
        }
        nextQuestion(true);

    }

    private void nextQuestion(boolean isFirstInit) {
        if (child == null || child.isEmpty()) return;
        if (!isFirstInit) {
            index++;
        }
        if (index >= child.size()) {//题目做完
            RxBus.get().post(C.REFRESH_LISTEN_LIST, id);
            ListenMakeResultActivity.startSecTestAct(mContext, id);
            finish();
            return;
        }
        titleTv.setText(getString(R.string.str_listen_parctice_title, index + 1, child.size()));
        nextQuestion(child.get(index));
    }

    private void play(final String url, final AnimationDrawable mAnimationDrawable) {
        player.play(mRxPermissions, rxDownload, url,
                new ICallBackImp() {
                    @Override
                    public void onSuccess(Object o) {
                        mAnimationDrawable.start();
                    }
                }, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAnimationDrawable.stop();
                        mAnimationDrawable.selectDrawable(0);
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
//                        return rxDownload.getDownloadRecord(url);
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
//                        player.setQueue(list, list.size() - 1);
//                        player.setCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                mAnimationDrawable.stop();
//                                mAnimationDrawable.selectDrawable(0);
//                            }
//                        });
//                        mAnimationDrawable.start();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                });
    }

    private void nextQuestion(ListenChildData data) {
        setNextClickAble(false);
        if (TextUtils.isEmpty(data.getFileAdd())) {
            Utils.setGone(listenImg);
        } else {
            //后台自动下载
            url = RetrofitProvider.TOEFLURL + data.getFileAdd();
            DownloadUtil.downloadService(url, rxDownload, mRxPermissions);
            Utils.setVisible(listenImg);
        }

        if (answerData == null) {
            answerData = new ArrayList<>();
        }
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        answerData.clear();
        mDataList.clear();
        pid = data.getId();
        questionTv.setText(data.getName());

        String[] split = null;
        String optionStr = data.getQuestSelect();
        if (optionStr.contains("\\r\\n")) {
            split = Utils.splitOption(optionStr);
        } else {
            split = Utils.splitOptionThroughN(optionStr);
        }
        String questType = data.getQuestType();
        type = 0;
        if (!TextUtils.isEmpty(questType)) {
            type = Integer.parseInt(questType);
        }
        //        表格 questType == 1
        if (type == 1) {
            formChoose(split);
            setNextClickAble(true);
            return;
        }

        //        拖动排序 questType == 2 或单选或多选
        choose(data, split, type);

    }

    private void formChoose(String[] split) {
        if (split == null || split.length == 0) return;
        String formOption = split[0];
        for (int i = 1, size = split.length; i < size; i++) {
            AnswerData ans = new AnswerData();
            ans.setFormOption(formOption);
            ans.setContent(split[i]);
            mDataList.add(ans);
        }
        if (mAdapter == null) {
            mAdapter = new ListenSecAnswerAdapter(mDataList, false);
            mAdapter.setListener(mListener);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.upDate(mDataList, false);
        }
    }

    private void choose(ListenChildData data, String[] split, int type) {

        String answer = data.getAnswer().trim();
        answer = Utils.removerepeatedchar(answer);
        answerNum = answer.length();
        isMultChoose = answerNum != 1;

        for (int i = 0, size = split.length; i < size; i++) {
            AnswerData ans = new AnswerData();
            ans.setOption(options[i]);
            ans.setContent(split[i]);
            mDataList.add(ans);
        }
        if (mAdapter == null) {
            mAdapter = new ListenSecAnswerAdapter(mDataList);
            mAdapter.setListener(mListener);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.upDate(mDataList);
        }

        mAdapter.setMultChooseAndSort(isMultChoose, type == 2);
    }

    @OnClick({R.id.commit_answer_goon_next_btn, R.id.listen_sec_test_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_answer_goon_next_btn:
                commitAnswer();
                break;
            case R.id.listen_sec_test_img:
                if (TextUtils.isEmpty(url)) return;
                if (player == null)
                    player = new MusicPlayer();
                AnimationDrawable mAnimationDrawable = (AnimationDrawable) ((ImageView) v).getDrawable();
                play(url, mAnimationDrawable);
                break;
            default:
                break;
        }
    }

    private void commitAnswer() {
        StringBuffer sb = new StringBuffer();
        if (type == 1) {
            for (AnswerData ans : mDataList) {
                String answer = ans.getSingleChooseAnswer();
                if (TextUtils.isEmpty(answer)) {
                    toastShort(R.string.str_please_choose);
                    return;
                }
                sb.append(answer);
            }
        } else {
            if (isMultChoose) {
                for (String ua : answerData) {
                    sb.append(ua);
                }
            } else {
                sb.append(userAnswer);
            }
        }
        addToCompositeDis(HttpUtil
                .chechAnswer(pid, id, sb.toString())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        setNextClickAble(false);
                        nextQuestion(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private OnItemClickListener mListener = new OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            String clickAnswer = mDataList.get(position).getOption();
            if (!isMultChoose) {
                userAnswer = clickAnswer;
                setNextClickAble(true);
            } else {
                if (answerData.contains(clickAnswer)) {
                    answerData.remove(clickAnswer);
                } else {
                    answerData.add(clickAnswer);
                }
                if (answerNum <= answerData.size()) {
                    setNextClickAble(true);
                } else {
                    setNextClickAble(false);
                }
            }
        }
    };
}
