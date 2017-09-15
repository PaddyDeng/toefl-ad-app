package io.dcloud.H58E83894.ui.make.bottom.wp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.data.make.WriteQuestionData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WriteQuestionActivity extends BaseActivity {

    public static void startWriteQuestionAct(Activity act, int num, String id) {
        Intent intent = new Intent(act, WriteQuestionActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, num);
        intent.putExtra(Intent.EXTRA_TEXT, id);
        act.startActivity(intent);
    }

    @BindView(R.id.write_question_title_txt)
    TextView mTitleTxt;
    @BindView(R.id.write_question_txt)
    TextView mQuestionTxt;

    private int num;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_question);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        num = intent.getIntExtra(Intent.EXTRA_INDEX, 1);
        id = intent.getStringExtra(Intent.EXTRA_TEXT);
        StringBuffer sb = new StringBuffer();
        if (num < 9) {
            sb.append(0);
        }
        sb.append(num);
        mTitleTxt.setText(getString(R.string.str_read_independent_topic_title, sb.toString()));
    }

    @OnClick({R.id.write_independent_start_practice_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_independent_start_practice_tv:
                WriteAnswerActivity.startWriteAnswer(WriteQuestionActivity.this, id, C.BELONG_INDENPDENT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == C.REQUEST_COMMIT_ANSWER) {
            RxBus.get().post(C.REFRESH_WRITE_LIST, num);
            finish();
        }
    }

    @Override
    protected void asyncUiInfo() {
        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil
                .independenceDetail(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<WriteQuestionData>() {
                    @Override
                    public void accept(@NonNull WriteQuestionData bean) throws Exception {
                        dismissLoadDialog();
                        PracticeData data = bean.getData();
                        if (data != null) {
                            mQuestionTxt.setText(data.getQuestion());
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
}
