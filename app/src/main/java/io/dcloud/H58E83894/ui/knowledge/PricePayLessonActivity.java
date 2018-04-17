package io.dcloud.H58E83894.ui.knowledge;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.data.LeidouReData;
import io.dcloud.H58E83894.data.PayData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.ui.common.SimpleLoginTipDialog;
import io.dcloud.H58E83894.utils.RegexValidateUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static android.content.DialogInterface.*;

public class PricePayLessonActivity extends BaseActivity {

    public static void startPre(Context c, String price, String id) {
        Intent intent = new Intent(c, PricePayLessonActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, price);
        intent.putExtra(Intent.EXTRA_TITLE, id);
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
        id = intent.getStringExtra(Intent.EXTRA_TITLE);
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
                        .getLeidouPay(Integer.parseInt(id.trim()), Integer.parseInt(price.trim()))
                        .subscribe(new Consumer<PayData>() {
                            @Override
                            public void accept(PayData bean) throws Exception {
                                dismissLoadDialog();

                                if(bean.getMessage().equals("雷豆不足，请及时充值！")){
//                                    showLoadDialog();
//                                    SimplePayDialog simplePayDialog = new SimplePayDialog();
//                                   simplePayDialog.

                                    final AlertDialog.Builder localBuilder = new AlertDialog.Builder(PricePayLessonActivity.this);
                                    localBuilder.setTitle("雷豆不足,是否前往充值？");
                                    localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                                        {
                                            DealActivity.startDealActivity(PricePayLessonActivity.this, "支付充值", "http://login.gmatonline.cn/cn/index?source=20&url=http://order.gmatonline.cn/pay/order/integral");
                                            finish();
                                        }
                                    });
                                    localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                                        {
                                            finish();
                                            /**
                                             * 确定操作
                                             * */
                                        }
                                    });

                                    /***
                                     * 设置点击返回键不会消失
                                     * */
                                    localBuilder.setCancelable(false).create();

                                    localBuilder.show();

//
                                }else {

                                toastShort(bean.getMessage());
                                Bundle bundle1=new Bundle();
                                bundle1.putInt("positon",bean.getCode());
                                Intent intent1=new Intent();
                                intent1.putExtras(bundle1);
                                PricePayLessonActivity.this.setResult(RESULT_OK,intent1 );
                                    finish();
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

    @Override
    public AnimType getAnimType() {
        return AnimType.ANIM_TYPE_UP_IN;
    }
}
