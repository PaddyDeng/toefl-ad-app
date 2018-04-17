package io.dcloud.H58E83894.ui.make.easyResource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.data.CorretData;
import io.dcloud.H58E83894.data.PayDatas;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class EaPricePayCorretActivity extends BaseActivity {

    public static void startPre(Context c, String price) {
        Intent intent = new Intent(c, EaPricePayCorretActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, price);
        c.startActivity(intent);
    }



   @BindView(R.id.pay_lesosn_if)
    TextView payLessonIf;

    private String price;
    private String id;
    private  int RESULT_OK = 99;
    private Context context;


    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        price = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_price_pay_dialog);
        payLessonIf.setText("确认支付"+price+"雷豆么?");
    }

    @OnClick({R.id.pay_lesosn_sure, R.id.pay_lesosn_no, R.id.pay_lesosn_diss})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_lesosn_sure:

                showLoadDialog();
                addToCompositeDis(HttpUtil
                        .getPlacess(200)
                        .subscribe(new Consumer<PayDatas>() {
                            @Override
                            public void accept(PayDatas bean) throws Exception {
                                dismissLoadDialog();

                                if(bean.getCode() == 1){
                                    initIdData();
                                    finish();
                                }else {
                                    toastShort("购买失败");
                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                               showLoadDialog();
                            }
                        }));

                break;
            case R.id.pay_lesosn_no:
                finish();
                break;
            case R.id.pay_lesosn_diss:
                finish();
                break;
            default:
                break;
        }
    }

    private void initIdData() {


        addToCompositeDis(HttpUtil
                .getPlaces(420).subscribe(new Consumer<CorretData>() {
                    @Override
                    public void accept(@NonNull CorretData data) throws Exception {
                        WaitDialog.getInstance(EaPricePayCorretActivity.this).dismissWaitDialog();
                        if (data.getCode() == 1) {
                            String contentId = data.getContentId();
                            EventBus.getDefault().post(contentId);
                            finish();
                        } else {
                            Utils.toastShort(EaPricePayCorretActivity.this, "使用失败，请再一次使用");
//                            dismiss();
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        finish();
                    }
                }));
    }


    @Override
    public AnimType getAnimType() {
        return AnimType.ANIM_TYPE_UP_IN;
    }
}
