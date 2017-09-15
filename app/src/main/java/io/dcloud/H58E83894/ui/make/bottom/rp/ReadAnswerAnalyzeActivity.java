package io.dcloud.H58E83894.ui.make.bottom.rp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.make.ReadQuestionData;

public class ReadAnswerAnalyzeActivity extends BaseActivity {

    public static void startAnswerAnalyzeAct(Context mContext, ReadQuestionData data, String userAnswer) {
        Intent intent = new Intent(mContext, ReadAnswerAnalyzeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.putExtra(Intent.EXTRA_TITLE, userAnswer);
        mContext.startActivity(intent);
    }

    private String userAnswer;
    private ReadQuestionData mReadQuestionData;
    private static final int CONTAINER = R.id.read_answer_fl_container;
    private ReadAnswerFragment mAnswerFragment;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        userAnswer = intent.getStringExtra(Intent.EXTRA_TITLE);
        mReadQuestionData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void initData() {
        mAnswerFragment = ReadAnswerFragment.getInstance(mReadQuestionData, userAnswer);
        replaceFragment(getSupportFragmentManager(), mAnswerFragment);
    }

    private void replaceFragment(FragmentManager fm, Fragment fragment) {
        String tag = "READ_ANSWER_ANALYZE";
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.findFragmentByTag(tag) == null) {
            transaction.replace(CONTAINER, fragment);
            transaction.addToBackStack(tag);
        } else {
            transaction.replace(CONTAINER, fm.findFragmentByTag(tag), tag);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_answer);
    }
}
