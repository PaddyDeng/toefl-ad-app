package io.dcloud.H58E83894.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/15  16:52.
 */

public class QuestionReportDialog extends BaseDialog {

    public static QuestionReportDialog getInstance(String id) {
        QuestionReportDialog dialog = new QuestionReportDialog();
        Bundle bundle = new Bundle();
        bundle.putString("ID", id);
        dialog.setArguments(bundle);
        return dialog;
    }

    private String id;
    @BindView(R.id.close_dialog_iv)
    ImageView close;
    @BindView(R.id.commit_feed_back)
    ImageView commit;
    @BindView(R.id.simple_et)
    EditText et;

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle b = getArguments();
        if (b == null) return;
        id = b.getString("ID");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    private void commit() {
        String txt = getEditTxt(et);
        if (TextUtils.isEmpty(txt)) {
            toastShort(R.string.str_post_remark_enter_content);
            return;
        }
        addToCompositeDis(HttpUtil.questionReport(id, txt)
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
                        toastShort(bean.getMessage());
                        if (getHttpCodeSucc(bean.getCode())) {
                            dismiss();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        toastShort(Utils.onError(throwable));
                    }
                }));
    }

    @Override
    protected int getContentViewLayId() {
        return R.layout.dialog_question_report_layout;
    }
}
