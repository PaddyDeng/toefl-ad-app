package io.dcloud.H58E83894.ui.make.grammar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.AnswerData;
import io.dcloud.H58E83894.data.make.GrammarData;
import io.dcloud.H58E83894.data.make.GrammarRecordData;
import io.dcloud.H58E83894.data.make.LearningBean;
import io.dcloud.H58E83894.data.make.QuestionBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.db.MakeTable;
import io.dcloud.H58E83894.db.RecordManager;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.common.TaskEndDialog;
import io.dcloud.H58E83894.ui.common.grammar.AnalyzeDialog;
import io.dcloud.H58E83894.ui.make.adapter.AnswerAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.JsonUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.TimeUtils;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GrammarTestActivity extends BaseActivity {

    public static void startGrammarTestActivity(Context mContext) {
        Intent intent = new Intent(mContext, GrammarTestActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, true);
        mContext.startActivity(intent);
    }

    @BindView(R.id.grammar_question_tv)
    TextView questionTv;
    @BindView(R.id.answer_recycler)
    RecyclerView answerRecycler;
    @BindView(R.id.commit_answer_btn)
    TextView commitBtn;
    @BindView(R.id.grammar_title_tv)
    TextView titleTv;
    private AnalyzeDialog mDialog;
    private String[] options = new String[]{"A", "B", "C", "D", "E", "F"};
    private List<AnswerData> mDataList;
    private AnswerAdapter mAdapter;
    private String correctAnswer;
    private String yourAnser;
    private String questionId;
    private String questionJson;
    private boolean nextQuestion = false;
    private TaskEndDialog mtaskDialog;
    private boolean seeResult;
    private int index = 0;
    private List<GrammarRecordData> mGrammarDatas;
    private Date mDate;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        seeResult = intent.getBooleanExtra(Intent.EXTRA_TEXT, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_test);
        if (seeResult) {
            commitBtn.setBackgroundResource(R.drawable.com_btn_selector);
            commitBtn.setClickable(true);
            commitBtn.setText(R.string.str_grammar_next_question);
        } else {
            commitBtnAble();
        }
    }

    @Override
    protected void initView() {
        initRecycler(answerRecycler, new LinearLayoutManager(this));
    }

    @Override
    protected void asyncUiInfo() {
        if (seeResult) {
            queryMakeResult();
            return;
        }
        nextQuestion = false;
        addToCompositeDis(HttpUtil.grammarLearn()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .map(new Function<GrammarData, GrammarData>() {
                    @Override
                    public GrammarData apply(@NonNull GrammarData data) throws Exception {
                        int num = RecordManager.getInstance().queryMakeNum();
                        titleTv.setText(getString(R.string.str_grammar_test_title, num + 1));
                        return data;
                    }
                })
                .subscribe(new Consumer<GrammarData>() {
                    @Override
                    public void accept(@NonNull GrammarData bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            questionJson = JsonUtil.toJson(bean);
                            QuestionBean questionBean = bean.getQuestion();
                            refreshUi(questionBean);
                            LearningBean learning = bean.getGrammarLearning();
                            if (learning == null) return;
                            if (mDialog == null && !TextUtils.isEmpty(learning.getAnswer())) {
                                mDialog = AnalyzeDialog.getInstance(learning.getAnswer());
                            }
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

    private void queryMakeResult() {
        Observable.just(1)
                .flatMap(new Function<Integer, ObservableSource<List<GrammarRecordData>>>() {
                    @Override
                    public ObservableSource<List<GrammarRecordData>> apply(@NonNull Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<GrammarRecordData>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<GrammarRecordData>> e) throws Exception {
                                e.onNext(RecordManager.getInstance().queryMakeGrammar());
                                e.onComplete();
                            }
                        });
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
        titleTv.setText(getString(R.string.str_grammar_test_title, index + 1));
        GrammarRecordData recordData = mGrammarDatas.get(index);
        GrammarData data = JsonUtil.fromJson(recordData.getJson(), new TypeToken<GrammarData>() {
        }.getType());

        LearningBean learning = data.getGrammarLearning();
        if (learning != null) {
            if (mDialog == null && !TextUtils.isEmpty(learning.getAnswer())) {
                mDialog = AnalyzeDialog.getInstance(learning.getAnswer());
            }
        }
        QuestionBean question = data.getQuestion();
        correctAnswer = question.getAnswer();
        yourAnser = recordData.getUserAnswer();
        refreshUi(question);
        mAdapter.setAnswer(correctAnswer, yourAnser);
    }


    private void commitBtnAble() {
        commitBtn.setBackgroundResource(R.drawable.no_clickable_bg);
        commitBtn.setClickable(false);
        commitBtn.setText(R.string.str_grammar_commit_answer);
    }

    private void refreshUi(QuestionBean questionBean) {
        if (questionBean == null) return;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (!seeResult) {
            correctAnswer = questionBean.getAnswer();
            questionId = questionBean.getId();
            commitBtnAble();
        }

        mDataList.clear();
        questionTv.setText(questionBean.getListeningFile());
        String[] split = Utils.splitOption(questionBean.getAlternatives());
        for (int i = 0, size = split.length; i < size; i++) {
            AnswerData data = new AnswerData();
            data.setOption(options[i]);
            data.setContent(split[i]);
            mDataList.add(data);
        }
        if (mAdapter == null) {
            mAdapter = new AnswerAdapter(mDataList);
            if (!seeResult)
                mAdapter.setListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        yourAnser = mDataList.get(position).getOption();
                        commitBtn.setBackgroundResource(R.drawable.com_btn_selector);
                        commitBtn.setClickable(true);
                    }
                });
            answerRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.upDate(mDataList);
        }
    }

    @OnClick({R.id.commit_answer_btn, R.id.test_analyze_tv, R.id.question_feed_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question_feed_back:
                showFeedBackDialog(questionId);
                break;
            case R.id.commit_answer_btn:
                if (seeResult) {
                    index++;
                    seeResult();
                    return;
                }
                if (nextQuestion) {
                    asyncUiInfo();
                } else {
                    commitAnswer();
                }
                break;
            case R.id.test_analyze_tv:
                if (mDialog != null)
                    mDialog.showDialog(getSupportFragmentManager());
                break;
        }
    }

    private void commitAnswer() {
        addToCompositeDis(HttpUtil.taskNext(C.GRAMMAR_LEARNING)
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
                        updata();
                        commitBtn.setText(R.string.str_grammar_next_question);
                        if (getHttpResSuc(bean.getCode())) {
                            nextQuestion = true;
                            RxBus.get().post(C.MAKE_GRAMMAR, C.MAKEING);
                        } else if (bean.getCode() == 2) {
                            RxBus.get().post(C.MAKE_GRAMMAR, C.MAKE_END);
                            //做题完成
                            if (mtaskDialog == null) {
                                mtaskDialog = new TaskEndDialog();
                            }
                            mtaskDialog.showDialog(getSupportFragmentManager());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                }));
    }

    private void updata() {
        mAdapter.setAnswer(correctAnswer, yourAnser);
        if (mDate == null) {
            mDate = new Date();
        }
        Observable.just(1)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer) throws Exception {
                        ContentValues values = new ContentValues();
                        values.put(MakeTable.USER_UID, GlobalUser.getInstance().getUserData().getUid());
                        values.put(MakeTable.GRAMMAR_ID, questionId);
                        values.put(MakeTable.GRAMMAR_JSON, questionJson);
                        values.put(MakeTable.YOU_RESULT, yourAnser);
                        values.put(MakeTable.MAKE_DATE, TimeUtils.longToString(mDate.getTime(), "yyyy-MM-dd"));
                        RecordManager.getInstance().insertGrammarData(values);
                        return integer;
                    }
                })
                .compose(new SchedulerTransformer<Integer>())
                .subscribe();
    }

}
