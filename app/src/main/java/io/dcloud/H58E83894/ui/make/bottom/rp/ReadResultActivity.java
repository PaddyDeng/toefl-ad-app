package io.dcloud.H58E83894.ui.make.bottom.rp;

import android.content.Context;
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
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.ReadAnswerTypeData;
import io.dcloud.H58E83894.data.make.ReadResultData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.common.ResetTipDialog;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.WaveLoadingView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ReadResultActivity extends BaseActivity {

    public static void startReadResultAct(Context c, String id, String type) {
        Intent intent = new Intent(c, ReadResultActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, id);
        intent.putExtra(Intent.EXTRA_TITLE, type);
        c.startActivity(intent);
    }

    @BindView(R.id.result_use_time)
    TextView usetTimeTv;
    @BindView(R.id.result_correct_avg)
    TextView resultAvgTv;
    @BindView(R.id.wave_result)
    WaveLoadingView mWave;
    private String id;
    private String type;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_TEXT);
        type = intent.getStringExtra(Intent.EXTRA_TITLE);
    }

    @Override
    protected void asyncUiInfo() {
        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil
                .readResult(id)
                .subscribe(new Consumer<ReadResultData>() {
                    @Override
                    public void accept(@NonNull ReadResultData data) throws Exception {
                        refreshUi(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi(ReadResultData data) {
        ReadAnswerTypeData type = data.getAnswerType();
        if (type != null) {
            String time = type.getTime();
            if (!TextUtils.isEmpty(time)) {
                String[] split = time.split(":");
                usetTimeTv.setText(Utils.format(Integer.parseInt(split[0])));
            }
            resultAvgTv.setText(type.getAccuracy() + "%");
            mWave.setAnimDuration(3000);
            mWave.setCenterTitle(getString(R.string.str_wave_center_des, type.getCorrertNum(), type.getSum()));
            mWave.setProgressValue(type.getAccuracy());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_result);
    }


    /**
     *分享    R.id.result_share_iv   case R.id.result_share_iv:
                                  new ShareDialog().showDialog(getSupportFragmentManager());
                                   break;
     */

    @OnClick({R.id.result_detail_tv, R.id.simulation_again_start})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.result_detail_tv:
                if (TextUtils.isEmpty(id)) return;
                ReadResultDetailActivity.startReadResultDetailAct(mContext, id);
                break;
            case R.id.simulation_again_start:
                ResetTipDialog.getInstance(new ICallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        //重考,先重置，在继续考。
                        reset();
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

    private void reset() {
        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil
                .resetRead(id)
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
                            RxBus.get().post(C.REFRESH_READ_LIST, id);
                            ReadQuestionActivity.startReadQuestionAct(mContext, id, type);
                            finish();
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
}
