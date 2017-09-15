package io.dcloud.H58E83894.ui.make.grammar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.make.GrammarData;
import io.dcloud.H58E83894.data.make.LearningBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.common.grammar.SimpleDialog;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class GrammarGuideActivity extends BaseActivity {

    private GrammarData mGrammarData;
    private SimpleDialog mDialog;
    @BindView(R.id.grammer_des_tv)
    TextView grammarDesTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_guide);
    }

    @Override
    protected void initData() {
        addToCompositeDis(HttpUtil.grammarLearn()
                .subscribe(new Consumer<GrammarData>() {
                    @Override
                    public void accept(@NonNull GrammarData bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            mGrammarData = bean;
                            refreshUi();
                        } else {
                            initData();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi() {
        if (mGrammarData == null) return;
        LearningBean learning = mGrammarData.getGrammarLearning();
        if (learning == null) return;
        grammarDesTv.setText(getString(R.string.str_grammar_des, learning.getName()));
        String answer = learning.getAnswer();
        if (!TextUtils.isEmpty(answer)) {
            mDialog = SimpleDialog.getInstance(answer);
        }
    }

    @OnClick({R.id.grammar_start_practice_tv, R.id.knowledge_explain_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grammar_start_practice_tv:
                forword(GrammarTestActivity.class);
                break;
            case R.id.knowledge_explain_tv:
                //弹框解释
                if (mDialog != null)
                    mDialog.showDialog(getSupportFragmentManager());
                break;
            default:
                break;
        }
    }
}
