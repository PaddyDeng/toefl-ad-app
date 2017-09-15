package io.dcloud.H58E83894.ui.make.core;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.core.CoreAnswerBean;
import io.dcloud.H58E83894.data.make.core.CoreData;
import io.dcloud.H58E83894.data.make.core.CoreQuestionData;
import io.dcloud.H58E83894.data.make.GrammarRecordData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.db.MakeTable;
import io.dcloud.H58E83894.db.RecordManager;
import io.dcloud.H58E83894.excep.CustomException;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.common.TaskEndDialog;
import io.dcloud.H58E83894.ui.make.adapter.CoreAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.JsonUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.TimeUtils;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * 核心词汇
 */
public class CoreTestActivity extends BaseCoreActivity {


    public static void startcoreTestActivity(Context mContext) {
        Intent intent = new Intent(mContext, CoreTestActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, true);
        mContext.startActivity(intent);
    }

    private String[] options = new String[]{"A", "B", "C", "D", "E", "F"};
    @BindView(R.id.core_word_tv)
    TextView coreWordTv;
    @BindView(R.id.core_phonetic_tv)
    TextView corePhoneticTv;
    @BindView(R.id.core_centertxt)
    TextView titleTxt;
    @BindView(R.id.core_recycler)
    RecyclerView mRecyclerView;
    private int recycleHeight;
    private CoreQuestionData mCoreQuestionData;
    private List<CoreAnswerBean> mBeanList;
    private CoreAdapter adapter;
    private TaskEndDialog mDialog;
    private String correctAnswer;
    private boolean seeResult = false;
    private List<GrammarRecordData> mGrammarDatas;
    private int index = 0;
    private RxDownload rxDownload;
    private MusicPlayer mPlayer;
    private String id;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        seeResult = intent.getBooleanExtra(Intent.EXTRA_TEXT, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxDownload = RxDownload.getInstance(mContext);
        setContentView(R.layout.activity_core_test);
    }

    private String url;
    private AnimationDrawable mAnimationDrawable;

    @OnClick({R.id.core_audio_img, R.id.question_feed_back})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.question_feed_back:
                showFeedBackDialog(id);
                break;
            case R.id.core_audio_img:
                if (TextUtils.isEmpty(url)) return;
                if (mAnimationDrawable == null) {
                    ((ImageView) view).setImageResource(R.drawable.audio_animlist);
                    mAnimationDrawable = (AnimationDrawable) ((ImageView) view).getDrawable();
                }
                if (mPlayer == null)
                    mPlayer = new MusicPlayer();
                playAnim(url, mAnimationDrawable, mPlayer, rxDownload);

                break;
            default:
                break;
        }
    }


    @Override
    protected void initView() {
        initRecycler(mRecyclerView, new LinearLayoutManager(mContext));
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mRecyclerView,this);
                recycleHeight = mRecyclerView.getMeasuredHeight();
            }
        });
        if (!seeResult)
            setTitle();
    }

    private void setTitle() {
        Observable.just(1)
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                                e.onNext(RecordManager.getInstance().queryCoreNum());
                                e.onComplete();
                            }
                        });
                    }
                })
                .compose(new SchedulerTransformer<Integer>())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        titleTxt.setText(getString(R.string.str_core_vocabulary_test_title, integer + 1));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    protected void asyncUiInfo() {
        if (seeResult) {
            loadlocalData();
            return;
        }
        addToCompositeDis(HttpUtil.keyWords()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<CoreData>() {
                    @Override
                    public void accept(@NonNull CoreData data) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(data.getCode())) {
                            refreshUi(data.getQuestion());
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

    private void loadlocalData() {
        Observable
                .create(new ObservableOnSubscribe<List<GrammarRecordData>>() {

                    @Override
                    public void subscribe(ObservableEmitter<List<GrammarRecordData>> e) throws Exception {
                        e.onNext(RecordManager.getInstance().queryCore());
                        e.onComplete();
                    }
                })
                .compose(new SchedulerTransformer<List<GrammarRecordData>>())
                .subscribe(new Consumer<List<GrammarRecordData>>() {
                    @Override
                    public void accept(@NonNull List<GrammarRecordData> datas) throws Exception {
                        index = 0;
                        mGrammarDatas = datas;
                        seeResult();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    private void seeResult() {
        if (mGrammarDatas == null || mGrammarDatas.size() <= index) {
            if (mGrammarDatas.size() <= 0) {
                toastShort(R.string.str_get_local_record_fail);
            }
            finishWithAnim();
            return;
        }
        titleTxt.setText(getString(R.string.str_grammar_test_title, index + 1));
        GrammarRecordData recordData = mGrammarDatas.get(index);
        CoreQuestionData data = JsonUtil.fromJson(recordData.getJson(), new TypeToken<CoreQuestionData>() {
        }.getType());
        userAnswer = recordData.getUserAnswer();
        refreshUi(data);
    }

    private void refreshUi(CoreQuestionData question) {
        if (question == null) return;
        id = question.getId();
        url = RetrofitProvider.TOEFLURL + question.getAlternatives();
        DownloadUtil.download(url, rxDownload, mRxPermissions, new RequestImp<DownloadStatus>() {
            @Override
            public void requestFail(String msg) {
            }
        });
        mCoreQuestionData = question;
        if (mBeanList == null) {
            mBeanList = new ArrayList<>();
        }
        mBeanList.clear();
        String name = question.getName();
        if (!TextUtils.isEmpty(name)) {
            coreWordTv.setText(name);
        }
        String answer = question.getAnswer();
        if (!TextUtils.isEmpty(answer)) {
            corePhoneticTv.setText(getString(R.string.str_core_phonetic, answer));
        }
        correctAnswer = question.getListeningFile();

        String[] split = Utils.splitOption(question.getArticle());
        for (int i = 0, size = split.length; i < size; i++) {
            CoreAnswerBean data = new CoreAnswerBean();
            data.setContent(split[i]);
            String option = options[i];
            data.setOption(option);
            if (seeResult) {
                data.setStatus(C.DEFALUT);
                if (TextUtils.equals(userAnswer, correctAnswer)) {
                    //正确答案
                    if (TextUtils.equals(userAnswer, option)) {
                        data.setStatus(C.CORRECT);
                    }
                } else {
                    //错误答案
                    if (TextUtils.equals(userAnswer, option)) {
                        data.setStatus(C.ERROR);
                    } else if (TextUtils.equals(correctAnswer, option)) {
                        data.setStatus(C.CORRECT);
                    }
                }
            }
            mBeanList.add(data);
        }
        if (adapter == null) {
            adapter = new CoreAdapter(mBeanList);
            adapter.setItemHeight(recycleHeight);
            initListener(adapter);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.update(mBeanList);
        }
    }

    private void initListener(final CoreAdapter adapter) {
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                boolean correct = false;
                CoreAnswerBean clickBean = mBeanList.get(position);
                userAnswer = clickBean.getOption();
                if (TextUtils.equals(clickBean.getOption(), correctAnswer)) {
                    clickBean.setStatus(C.CORRECT);
                    //正确
                    correct = true;
                } else {
                    //错误
                    for (CoreAnswerBean bean : mBeanList) {
                        if (TextUtils.equals(bean.getOption(), correctAnswer)) {
                            bean.setStatus(C.CORRECT);
                        } else if (bean == clickBean) {
                            bean.setStatus(C.ERROR);
                        } else {
                            bean.setStatus(C.DEFALUT);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
//                if (correct) {
//                    //获取下一道题目
//                    next();
//                } else {
                //2017.8.22 不管正确错误都要显示单词意译
                    CoreDetailActivity.startCoreDetail(CoreTestActivity.this, mCoreQuestionData, index + 1);
//                }
            }
        });
    }

    private void next() {
        index++;
        if (seeResult) {
            seeResult();
        } else {
            nextQuestion();
        }
    }

    private Date mDate;
    private String userAnswer;

    private void update() {
        if (mDate == null) {
            mDate = new Date();
        }
        final ContentValues values = new ContentValues();
        values.put(MakeTable.USER_UID, GlobalUser.getInstance().getUserData().getUid());
        values.put(MakeTable.GRAMMAR_ID, mCoreQuestionData.getId());
        values.put(MakeTable.GRAMMAR_JSON, JsonUtil.toJson(mCoreQuestionData));
        values.put(MakeTable.YOU_RESULT, userAnswer);
        values.put(MakeTable.MAKE_DATE, TimeUtils.longToString(mDate.getTime(), "yyyy-MM-dd"));
        Observable.just(1)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer) throws Exception {
                        RecordManager.getInstance().insertCoreData(values);
                        return integer;
                    }
                })
                .compose(new SchedulerTransformer<Integer>())
                .subscribe();
        setTitle();
    }

    private void nextQuestion() {
        addToCompositeDis(HttpUtil.taskNext(C.CORE_WORD)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .flatMap(new Function<ResultBean, ObservableSource<CoreData>>() {
                    @Override
                    public ObservableSource<CoreData> apply(@NonNull ResultBean bean) throws Exception {
                        update();
                        if (getHttpResSuc(bean.getCode())) {
                            return HttpUtil.keyWords();
                        } else if (bean.getCode() == 2) {
                            //做题完成
                            throw new CustomException("test end");
                        }
                        return null;
                    }
                })
                .subscribe(new Consumer<CoreData>() {
                    @Override
                    public void accept(@NonNull CoreData data) throws Exception {
                        dismissLoadDialog();
                        RxBus.get().post(C.MAKE_CORE_REFRESH, C.MAKEING);
                        if (getHttpResSuc(data.getCode())) {
                            refreshUi(data.getQuestion());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        if (throwable instanceof CustomException) {
                            //弹出结束框
                            RxBus.get().post(C.MAKE_CORE_REFRESH, C.MAKE_END);
                            if (mDialog == null) {
                                mDialog = new TaskEndDialog();
                            }
                            mDialog.showDialog(getSupportFragmentManager());
                        }
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == C.CORE_REQUEST_CODE) {
            next();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
