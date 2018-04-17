package io.dcloud.H58E83894.ui.make.bottom.rp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.AnswerData;
import io.dcloud.H58E83894.data.make.ReadArticle;
import io.dcloud.H58E83894.data.make.ReadQuestion;
import io.dcloud.H58E83894.data.make.ReadQuestionData;
import io.dcloud.H58E83894.excep.CustomException;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.common.DetailDialog;
import io.dcloud.H58E83894.ui.make.adapter.ReadAnswerAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.CustomerWebView;
import io.dcloud.H58E83894.weiget.GeneralView;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.tencent.bugly.Bugly.enable;

public class ReadQuestionActivity extends BaseActivity {

    public static void startReadQuestionAct(Context mContext, String id, String belong) {
        Intent intent = new Intent(mContext, ReadQuestionActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        intent.putExtra(Intent.EXTRA_TITLE, belong);
        mContext.startActivity(intent);
    }

    @BindView(R.id.read_question_title_txt)
    TextView titleTv;
    @BindView(R.id.read_question_customer_web_view)
    CustomerWebView mWebView;
    @BindView(R.id.read_question_tv)
    GeneralView mGeneralView;
    @BindView(R.id.answer_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.shwo_answer_tv)
    TextView answerTv;
    @BindView(R.id.last_question_tv)
    TextView lastQuestionTv;
    @BindView(R.id.next_question_tv)
    TextView nextQuestionTv;
    @BindView(R.id.question_num_tv)
    TextView questionNumTv;
    @BindView(R.id.mult_type_select)
    TextView multTypeTv;
    private String id;
    private String belong;
    private DetailDialog mDialog;
    private ReadAnswerAdapter mAdapter;
    private List<AnswerData> mDataList;
    private String[] options = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I"};
    private String nextId;
    private String upId;
    private String userAnswer;
    private String contentId;
    private String lastsId;
    private String pid;
    private String correctAnswer;
    private String userAnswers;
    private long startTime;
    private ReadQuestionData readQuestionData;
    private String topicType;
    private String currentType;
    private int multTypeLen;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_INDEX);
        belong = intent.getStringExtra(Intent.EXTRA_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_question);
    }

    @Override
    protected void asyncUiInfo() {
        asyncData(id);
    }

    private void asyncData(String id) {
        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil
                .readQuestionDetail(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean<ReadQuestionData>>() {
                    @Override
                    public void accept(@NonNull ResultBean<ReadQuestionData> bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            refreshUi(bean.getData());
                        } else {
                            toastShort(bean.getMessage());
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

    @Override
    protected void initView() {
        initRecycler(mRecyclerView, new LinearLayoutManager(mContext));
    }

    private void refreshUi(ReadQuestionData data) {
        initBtnEnable(false);
        readQuestionData = data;
        ReadArticle article = data.getArticle();
        ReadQuestion question = data.getQuestion();
        nextId = data.getNextId();
        upId = data.getUpId();
        userAnswer = data.getUserAanswer();
        if (!TextUtils.isEmpty(data.getNextId()) && TextUtils.equals("0", data.getNextId())) {
            nextId = "";
        }

        if (!TextUtils.isEmpty(data.getUpId()) && TextUtils.equals("0", data.getUpId())) {
            upId = "";
        }

        contentId = data.getContentId();
        if (article == null || question == null) return;
        String[] split = null;
        String optionStr = question.getAnswerA();
        if (optionStr.contains("\\r\\n")) {
            split = Utils.splitOption(optionStr);
        } else {
            split = Utils.splitOptionThroughN(optionStr);
        }
        String questionType = question.getQuestionType();
        mWebView.setText(article.getQuestion(), question.getPostionD(), question.getPostionW(), questionType, split);
        mGeneralView.setText(question.getQuestion());
        titleTv.setText(article.getTitle());
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = DetailDialog.getInstance(article.getTitle(), data);

        pid = question.getPid();
        correctAnswer = question.getAnswer().trim();
        if (correctAnswer.trim().length() > 1) {
            topicType = C.MULT_CHOOSE;
        } else {
            topicType = "";
        }
        startTime = System.currentTimeMillis();
        questionNumTv.setText(getString(R.string.str_question_num, question.getTitle(), data.getCount()));
            initRecyclerData(split, questionType, question.getAnswerB());
    }

    private void initBtnEnable(boolean enable) {

//        lastQuestionTv.setClickable(enable);
//        lastQuestionTv.setSelected(enable);
        answerTv.setClickable(enable);
        answerTv.setSelected(enable);
        nextQuestionTv.setClickable(enable);
        nextQuestionTv.setSelected(enable);
    }

    private void initRecyclerData(String[] split, String type, String b) {
        currentType = type;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.clear();

        int index = 0;
        for (String content : split) {
            if (TextUtils.isEmpty(content)) {
                continue;
            }
            if (TextUtils.isEmpty(content.trim())) {
                continue;
            }
//            log("=" + content + "=");
            AnswerData data = new AnswerData();
            data.setOption(options[index++]);
            data.setType(TextUtils.equals(C.MULT_CHOOSE, topicType) ? C.MULT_CHOOSE : type);
            if (TextUtils.equals(type, "6") || TextUtils.equals("9", type)) {
                //答题类型： 插入
                data.setContent(b);
            } else {
                data.setContent(content);
            }
            mDataList.add(data);
        }


        if (TextUtils.equals("7", type)) {//type等于7多类型设置
            topicType = "";
            initBtnEnable(true);
            multTypeChoose(b);
            Utils.setVisible(multTypeTv);
            return;
        }
        Utils.setGone(multTypeTv);

        if (mAdapter == null) {
            mAdapter = new ReadAnswerAdapter(mDataList);
                mAdapter.setListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        initBtnEnable(true);
                        userAnswers = options[position];
                    }
                });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.upDate(mDataList);
        }
    }

    private void multTypeChoose(String b) {
        String[] questionB = null;
        if (b.contains("\\r\\n")) {
            questionB = Utils.splitOption(b);
        } else {
            questionB = Utils.splitOptionThroughN(b);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, size = questionB.length; i < size; i++) {
            if (i < size - 1)
                sb.append(TextUtils.concat(options[i], "：", questionB[i], "\n"));
            else
                sb.append(TextUtils.concat(options[i], "：", questionB[i]));
        }
        multTypeTv.setText(sb.toString());
        multTypeLen = questionB.length;
        if (mAdapter == null) {
            mAdapter = new ReadAnswerAdapter(mDataList, false, multTypeLen);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.upMultData(mDataList, multTypeLen);
        }
    }

    @OnClick({R.id.see_question_read_tv, R.id.next_question_tv, R.id.shwo_answer_tv, R.id.last_question_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.see_question_read_tv:
                if (mDialog == null) return;
                mDialog.showDialog(getSupportFragmentManager());
                break;

            case R.id.shwo_answer_tv://显示答案
                if (readQuestionData == null) return;
                ReadAnswerAnalyzeActivity.startAnswerAnalyzeAct(mContext, readQuestionData, multChooseAnswer());
                break;

            case R.id.next_question_tv://下一题

                String answer = multChooseAnswer();
                if (TextUtils.isEmpty(answer)) {
                    toastShort(R.string.str_please_choose_answer);
                    return;
                }
                if (TextUtils.isEmpty(nextId)) {
                    commitAnserAndJump();
                    Log.i("sss", nextId);
                } else {
                    commitAnswerAndAsyncData();
                }
                break;

            case R.id.last_question_tv://上一题
                lastsId = String.valueOf(Integer.parseInt(contentId)-1);
                if ((Integer.parseInt(nextId)-Integer.parseInt(id)) == 2) {
                    lastQuestionTv.setClickable(enable);
                    lastQuestionTv.setSelected(enable);
                    toastShort(R.string.str_please_already_first);
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("上一题");
                builder.setMessage("小雷哥请问您是否重做上一题？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            commitAnswerAndAsyncDatas();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            default:
                break;
        }
    }

    private String multChooseAnswer() {
        StringBuffer sb = new StringBuffer();
        if (Utils.isMultChosse(topicType)) {//多选
            //答案无顺序
            int index = 0;
            for (AnswerData answerData : mDataList) {
                if (answerData.isSelected()) {
                    sb.append(options[index]);
                }
                index++;
            }
            //答案按顺序
//            String[] ans = new String[8];
//            for (AnswerData answerData : mDataList) {
//                ans[answerData.getChooseSer()] = answerData.getOption();
//            }
//            for (int i = 1, size = ans.length; i < size; i++) {
//                String opt = ans[i];
//                if (TextUtils.isEmpty(opt)) continue;
//                sb.append(opt);
//            }
        } else if (TextUtils.equals(currentType, "7")) {
            List[] ansArr = new List[multTypeLen];
            for (int i = 0; i < multTypeLen; i++) {
                ansArr[i] = new ArrayList();
            }
            List<String> asList = Arrays.asList(options);
            for (AnswerData data : mDataList) {
                String answer = data.getReadMultAnswer();
                if (TextUtils.isEmpty(answer))
                    continue;
                else
                    ansArr[asList.indexOf(answer)].add(data.getOption());
            }
            for (int i = 0; i < multTypeLen; i++) {
                List<String> li = ansArr[i];
                for (String s : li) {
                    if (s.isEmpty()) continue;
                    sb.append(s);
                }
                if (i < multTypeLen - 1) {
                    sb.append("\r\n");
                }
            }
        } else {
            sb.append(userAnswers);
        }
        return sb.toString();
    }

    private void commitAnserAndJump() {
        showLoadDialog();
        int useTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        addToCompositeDis(HttpUtil
                .saveRead(contentId, pid, multChooseAnswer(), correctAnswer, belong, String.valueOf(useTime))
                .compose(new SchedulerTransformer<ResultBean>())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            RxBus.get().post(C.REFRESH_READ_LIST, id);
                            ReadResultActivity.startReadResultAct(mContext, id, belong);
                            finish();
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                },  new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                })
        );
    }

    private void commitAnswerAndAsyncData() {
        showLoadDialog();
        int useTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        addToCompositeDis(HttpUtil
                .saveRead(contentId, pid, multChooseAnswer(), correctAnswer, belong, String.valueOf(useTime))
                .flatMap(new Function<ResultBean, ObservableSource<ResultBean<ReadQuestionData>>>() {
                    @Override
                    public ObservableSource<ResultBean<ReadQuestionData>> apply(@NonNull ResultBean bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            return HttpUtil.readNextQuestion(nextId);
                        }
                        throw new CustomException(bean.getMessage());
                    }
                })
                .compose(new SchedulerTransformer<ResultBean<ReadQuestionData>>())
                .subscribe(new Consumer<ResultBean<ReadQuestionData>>() {
                    @Override
                    public void accept(@NonNull ResultBean<ReadQuestionData> bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            RxBus.get().post(C.REFRESH_READ_LIST, id);
                            refreshUi(bean.getData());
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                })
        );

    }


    private void commitAnswerAndAsyncDatas() {

        showLoadDialog();
        int useTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        addToCompositeDis(HttpUtil
                .saveRead(contentId, pid, multChooseAnswer(), correctAnswer, belong, String.valueOf(useTime))
                .flatMap(new Function<ResultBean, ObservableSource<ResultBean<ReadQuestionData>>>() {
                    @Override
                    public ObservableSource<ResultBean<ReadQuestionData>> apply(@NonNull ResultBean bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
//                            mAdapter = new ReadAnswerAdapter(mDataList, false, multTypeLen);
//                            mRecyclerView.setAdapter(mAdapter);
                            return HttpUtil.readNextQuestion(upId);
                        }
                        throw new CustomException(bean.getMessage());
                    }
                })
                .compose(new SchedulerTransformer<ResultBean<ReadQuestionData>>())
                .subscribe(new Consumer<ResultBean<ReadQuestionData>>() {
                    @Override
                    public void accept(@NonNull ResultBean<ReadQuestionData> bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            RxBus.get().post(C.REFRESH_READ_LIST, id);
                            refreshUi(bean.getData());
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                })
        );

    }
}
