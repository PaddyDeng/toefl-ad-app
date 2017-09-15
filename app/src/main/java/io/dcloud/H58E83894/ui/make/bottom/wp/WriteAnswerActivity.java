package io.dcloud.H58E83894.ui.make.bottom.wp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.C;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WriteAnswerActivity extends BaseActivity {

    public static void startWriteAnswer(BaseActivity c, String id, String type) {
        Intent intent = new Intent(c, WriteAnswerActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        intent.putExtra(Intent.EXTRA_TEXT, type);
        c.startActivityForResult(intent, C.REQUEST_COMMIT_ANSWER);
    }

    @BindView(R.id.write_answer_et)
    EditText contentEt;
    private String id;
    private String type;
    private long recordEnterTime;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_INDEX);
        type = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);
        recordEnterTime = System.currentTimeMillis();
    }

    @OnClick({R.id.commit_write_answer_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_write_answer_btn:
                commitAnswer();
                break;
            default:
                break;
        }
    }

    private void commitAnswer() {
        if (needLogin()) return;
        long useTime = (System.currentTimeMillis() - recordEnterTime) / 1000;
        String content = getEditText(contentEt);
        if (TextUtils.isEmpty(content)) {
            toastShort(R.string.str_please_enter_content);
            return;
        }
        addToCompositeDis(HttpUtil
                .commitWrite(id, content, type, String.valueOf(useTime))
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
                        if (getHttpResSuc(bean.getCode())) {
                            toastShort(bean.getMessage());
                            setResult(RESULT_OK);
                            finishWithAnim();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                }));
    }
}
