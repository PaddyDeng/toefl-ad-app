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
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.make.NowResultBean;
import io.dcloud.H58E83894.data.make.ResultData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.ui.common.DetailDialog;
import io.dcloud.H58E83894.ui.common.ResetTipDialog;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WriteResultActivity extends BaseActivity {

    public static void startResult(Activity act, int num, String id, String type) {
        Intent intent = new Intent(act, WriteResultActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, id);
        intent.putExtra(Intent.EXTRA_TITLE, type);
        intent.putExtra(Intent.EXTRA_INDEX, num);
        act.startActivity(intent);
    }

    private int num;
    private String id;
    private String type;
    private String content;
    private DetailDialog dialog;
    @BindView(R.id.write_result_title_txt)
    TextView titleTv;
    @BindView(R.id.result_word_num_tv)
    TextView wordNum;
    @BindView(R.id.result_use_time_show)
    TextView useTime;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        num = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
        id = intent.getStringExtra(Intent.EXTRA_TEXT);
        type = intent.getStringExtra(Intent.EXTRA_TITLE);

        StringBuffer sb = new StringBuffer();
        if (num < 9) {
            sb.append(0);
        }
        sb.append(num);
        if (TextUtils.equals(type, C.BELONG_INDENPDENT)) {
            titleTv.setText(getString(R.string.str_read_independent_topic_title, sb.toString()));
        } else if (TextUtils.equals(type, C.BELONG_TPO)) {
            titleTv.setText(getString(R.string.str_read_tpo_title, sb.toString()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_result);
    }

    @Override
    protected void asyncUiInfo() {
        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil
                .writeResult(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultData>() {
                    @Override
                    public void accept(@NonNull ResultData data) throws Exception {
                        dismissLoadDialog();
                        refreshUi(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                }));
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

    /**
     *分享    R.id.result_share_iv   case R.id.result_share_iv:
     new ShareDialog().showDialog(getSupportFragmentManager());
     break;
     */
    @OnClick({R.id.result_detail_tv, R.id.simulation_again_start, R.id.reservation_write_correction})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reservation_write_correction:
                DealActivity.startDealActivity(mContext, "", "http://p.qiao.baidu.com/im/index?siteid=7905926&ucid=18329536&cp=&cr=&cw=", C.DEAL_ADD_HEADER);
                break;
            case R.id.result_detail_tv:
                if (dialog == null) {
                    dialog = DetailDialog.getInstance(content, titleTv.getText().toString());
                }
                dialog.showDialog(getSupportFragmentManager());
                break;
            case R.id.simulation_again_start:
                ResetTipDialog.getInstance(new ICallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        WriteAnswerActivity.startWriteAnswer(WriteResultActivity.this, id, type);
                    }

                    @Override
                    public void onFail() {
                    }
                }).showDialog(getSupportFragmentManager());
                break;

            default:
                break;
        }
    }

    private void refreshUi(ResultData data) {
        if (data == null) return;
        wordNum.setText(String.valueOf(data.getNum()));
        NowResultBean result = data.getNowResult();
        if (result == null) return;
        useTime.setText(getString(R.string.str_use_time, Utils.format(Integer.parseInt(result.getElapsedTime()))));
        content = result.getAnswer();
    }
}
