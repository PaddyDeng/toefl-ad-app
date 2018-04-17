package io.dcloud.H58E83894.ui.center.leidou.recharge;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.gensee.report.ReportInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.LeidouRechargeData;
import io.dcloud.H58E83894.data.PayData;
import io.dcloud.H58E83894.data.PayDatas;
import io.dcloud.H58E83894.data.PayJson;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.pay.zfb.PayResult;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.ui.common.RecordProxy;
import io.dcloud.H58E83894.ui.knowledge.PricePayLessonActivity;
import io.dcloud.H58E83894.ui.prelesson.PreProGramLessonActivity;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 充值中心
 * */

public class RechargeActivity extends BaseActivity {


    public static void RechargeActi(Context c, String leidouSum) {
        Intent intent = new Intent(c, RechargeActivity.class);
        intent.putExtra(Intent.EXTRA_TITLE, leidouSum);
        c.startActivity(intent);
    }


    @BindView(R.id.leidou_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.leidou_recharge)
    TextView leidouRecharge;
    @BindView(R.id.tv_recharge_money)
    TextView tv_recharge_money;
    String orderInfo = null;   // 订单信息
    private int orderId;   // 订单信息
    private static final int SDK_PAY_FLAG = 1;
    private String leidouSum;


    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        leidouSum = intent.getStringExtra(Intent.EXTRA_TITLE);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        forword(RechargeCompleteActivity.class);
//                        endPayBack();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
//                case WX_PAY_FLAG://微信回调
//                    Toast.makeText(PayTypeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                    endPayBack();
//                    //执行删除购物车中该商品
//                    if (pIdList != null) {
//                        deleteMultGoods();
//                    }
//                    finish();
//                    break;
            }
        }

    };



    @Override
    public  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leidou_recharege);
        initRecycle();
        EventBus.getDefault().register(this);
//        leidouView.set

    }

    private void initRecycle() {
        tv_recharge_money.setText(HtmlUtil.fromHtml(getString(R.string.str_leidou_nums, leidouSum)));
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        String[] array = getResources().getStringArray(R.array.leidou_money_name);
        String[] arrays = getResources().getStringArray(R.array.leidou_sum_name);
        final RechargeAdapter adapter = new RechargeAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.replaceAll(initTestType(array, arrays), this);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ItemModel model) {
        final String money = model.data;

        leidouRecharge.setBackground(getResources().getDrawable(R.color.color_text_green));
        final UserData datas = GlobalUser.getInstance().getUserData();
        leidouRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCompositeDis(HttpUtil
                        .getZfb(datas.getUid(), money)
                        .subscribe(new Consumer<PayDatas>() {
                            @Override
                            public void accept(PayDatas bean) throws Exception {

                                orderId = bean.getOrderId();
                                initPay();
                                String orderInfo = null;   // 订单信息
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

//                                showLoadDialog();
                            }
                        }));
            }
        });




    }

    private void initPay() {

        addToCompositeDis(HttpUtil
                .getOrderInfo(orderId)
                .subscribe(new Consumer<PayDatas>() {
                    @Override
                    public void accept(PayDatas bean) throws Exception {
                        orderInfo = bean.getContent();

//                        MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
//                        builder.setAppid("123")
//                                .setRsa("456")//根据情况设置Rsa2与Rsa
//                                .setMoney("0.01")//单位时分
//                                .setTitle("支付测试")
//                                .setOrderTradeId("487456")//从服务端获取
//                                .setNotifyUrl("fdsfasdf")//从服务端获取
//                                .build()
//                                .toALiPay(MainActivity.this);

                        recAudio();
                        String orderInfo = null;   // 订单信息
                        Log.i("OneViewHolder =1 ", bean+"ddd");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("OneViewHolder =1 ", throwable+"ddd");
//                                showLoadDialog();
                    }
                }));
    }




//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getAdapterClickInfo(ItemModel model) {
//        int money = model.data;
//        tv_recharge_money.setText(money+"");
//    }

    private ArrayList<ItemModel> initTestType(String[] names, String[] arrays) {
//        List<LeidouRechargeData> mDatass = new ArrayList<>();
        ArrayList<ItemModel> mDatas = new ArrayList<>();
        for (int i = 0, size = names.length; i < size; i++) {
//            LeidouRechargeData data = new LeidouRechargeData();
//            data.setMoney(names[i]);
            String moneys = (names[i]);
//            data.setSum(arrays[i]);
//            mDatass.add(data);
            mDatas.add(new ItemModel(ItemModel.ONE, moneys));
        }
        mDatas.add(new ItemModel(ItemModel.TWO, null));

        return mDatas;
    }


    private void recAudio() {
        mRxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                        pay_zfb();
//                        if (aBoolean)
//                            RecordProxy.showRecordDialog(mContext, VoiceManager.getInstance(mContext), new ICallBack<String>() {
//                                @Override
//                                public void onSuccess(String absFilePath) {
//
//                                    pay_zfb();
//                                }
//
//                                @Override
//                                public void onFail() {
//
//                                }
//                            });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }

    public void pay_zfb() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
//    private void click(TestTypeData data) {
//        switch (data.getFlag()) {
//            case TestTypeData.TIME://考试时间
//                break;
//            case TestTypeData.ADDRESS://考试地址
//                break;
//            case TestTypeData.CONTENT://考试内容
//                break;
//            case TestTypeData.PROCESS://报名流程
//                break;
//            case TestTypeData.PRICE://考试费用
//                break;
//            case TestTypeData.REVIEW://成绩复议
//                break;
//            case TestTypeData.CARD://考试证件
//                break;
//            case TestTypeData.TAKE://转考退考
//                break;
//            case TestTypeData.WAY://送分方式
//                break;
//            default:
//                break;
//        }
//    }



//    private void initDialog() {
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this);
//        View view = View
//                .inflate(this, R.popup_window_ranking_classify.activity_leidou_pay_dialog, null);
//        builder.setTitle("雷豆不足,是否前往充值？");
//        builder.setView(view);
//        builder.setCancelable(true);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
//            {
//                DealActivity.startDealActivity(RechargeActivity.this, "支付充值", "http://login.gmatonline.cn/cn/index?source=20&url=http://order.gmatonline.cn/pay/order/integral");
//                finish();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
//            {
//                finish();
//                /**
//                 * 确定操作
//                 * */
//            }
//        });
//
//    }
    //dui
}
