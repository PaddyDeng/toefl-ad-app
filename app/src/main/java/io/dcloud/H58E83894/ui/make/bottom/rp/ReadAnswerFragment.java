package io.dcloud.H58E83894.ui.make.bottom.rp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.data.make.AnswerData;
import io.dcloud.H58E83894.data.make.ReadArticle;
import io.dcloud.H58E83894.data.make.ReadQuestion;
import io.dcloud.H58E83894.data.make.ReadQuestionData;
import io.dcloud.H58E83894.ui.common.DetailDialog;
import io.dcloud.H58E83894.ui.make.adapter.ReadAnswerAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.CustomerWebView;
import io.dcloud.H58E83894.weiget.GeneralView;

/**
 * Created by fire on 2017/8/3  17:33.
 */

public class ReadAnswerFragment extends BaseFragment {

    @BindView(R.id.question_num_tv)
    TextView questionNumTv;
    @BindView(R.id.read_question_customer_web_view)
    CustomerWebView mWebView;
    @BindView(R.id.frag_read_question_tv)
    GeneralView mGeneralView;
    @BindView(R.id.answer_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.answer_mult_type_select)
    TextView answerMultTv;

    private String userAnswer;
    private String correctAnswer;
    private ReadQuestionData mReadQuestionData;
    private DetailDialog mDialog;
    private ReadAnswerAdapter mAdapter;
    private List<AnswerData> mDataList;
    private String[] options = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I"};

    public static ReadAnswerFragment getInstance(ReadQuestionData data, String userAnswer) {
        ReadAnswerFragment fragment = new ReadAnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("DATA", data);
        bundle.putString("ANSWER", userAnswer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getArgs() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        userAnswer = arguments.getString("ANSWER");
        mReadQuestionData = arguments.getParcelable("DATA");
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_read_answer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler(mRecyclerView, new LinearLayoutManager(getActivity()));
        if (mReadQuestionData == null) return;
        ReadArticle article = mReadQuestionData.getArticle();
        ReadQuestion question = mReadQuestionData.getQuestion();
        if (question == null || article == null) return;
        String[] split = null;
        String optionStr = question.getAnswerA();
        if (optionStr.contains("\\r\\n")) {
            split = Utils.splitOption(optionStr);
        } else {
            split = Utils.splitOptionThroughN(optionStr);
        }
        String questionType = question.getQuestionType();
        mWebView.setText(article.getQuestion(), question.getPostionD(), question.getPostionW(), questionType, split);

        mDialog = DetailDialog.getInstance(article.getTitle(), mReadQuestionData);

        questionNumTv.setText(getString(R.string.str_question_num, question.getTitle(), mReadQuestionData.getCount()));
        if (TextUtils.equals("2", questionType)) {
            mGeneralView.setText(TextUtils.concat(question.getQuestion(), "<p>", question.getHint(), "</p>").toString());
        } else {
            mGeneralView.setText(question.getQuestion());
        }
//        mGeneralView.setText(question.getQuestion());
        correctAnswer = question.getAnswer().trim();
        initRecyclerData(split, questionType, question.getAnswerB());
    }

    private void initRecyclerData(String[] split, String type, String b) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.clear();
        boolean isMultChoose = correctAnswer.trim().length() > 1;
        for (int i = 0, size = split.length; i < size; i++) {
            String content = split[i];
            if (TextUtils.isEmpty(content)) {
                continue;
            }
            AnswerData data = new AnswerData();
            data.setOption(options[i]);
            data.setType(isMultChoose ? C.MULT_CHOOSE : type);
            if (TextUtils.equals(type, "6") || TextUtils.equals("9", type)) {
                //答题类型： 插入
                data.setContent(b);
            } else {
                data.setContent(content);
            }
            mDataList.add(data);
        }
        if (TextUtils.equals(type, "7")) {
            Utils.setVisible(answerMultTv);
            multTypeChoose(b);
            return;
        }
        Utils.setGone(answerMultTv);
        if (mAdapter == null) {
            mAdapter = new ReadAnswerAdapter(mDataList);
            mRecyclerView.setAdapter(mAdapter);

        } else {
            mAdapter.upDate(mDataList);
        }
        if (isMultChoose) {
            mAdapter.setMultChoose(correctAnswer, userAnswer);
        } else {
            mAdapter.setAnswer(correctAnswer, userAnswer);
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
        int le = questionB.length;
        for (int i = 0; i < le; i++) {
            if (i < le - 1)
                sb.append(TextUtils.concat(options[i], "：", questionB[i], "\n"));
            else
                sb.append(TextUtils.concat(options[i], "：", questionB[i]));
        }
        answerMultTv.setText(sb.toString());

        if (mAdapter == null) {
            mAdapter = new ReadAnswerAdapter(mDataList, false, le);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.upMultData(mDataList, le);
        }
        mAdapter.setAnswer(correctAnswer);
    }


    @OnClick({R.id.see_question_read_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.see_question_read_tv:
                if (mDialog == null) return;
                mDialog.showDialog(getChildFragmentManager());
                break;
            default:
                break;
        }
    }
}
