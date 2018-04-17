package io.dcloud.H58E83894.ui.center.leidou;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.LeidouReData;
import io.dcloud.H58E83894.data.LeidouReData.DetailsBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.pay.zfb.PayResult;
import io.dcloud.H58E83894.ui.center.leidou.recharge.RechargeActivity;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.weiget.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static io.dcloud.H58E83894.ui.center.leidou.recharge.RechargeActivity.RechargeActi;

public class MyLeidouActivity extends BaseActivity {

    @BindView(R.id.deal_title)
    TextView titleName;
    @BindView(R.id.practice_head_iv)
    CircleImageView headIv;//头像
    @BindView(R.id.name)
    TextView headName;//人名
    @BindView(R.id.leidouNum)
    TextView leiNum;//雷总数
    @BindView(R.id.leidouRecycler)
    RecyclerView leiRecycler;
    @BindView(R.id.leidou_explain)
    TextView leiExplain;
    @BindView(R.id.more)//显示更多
    TextView more;
    @BindView(R.id.little)
    TextView little;
    @BindView(R.id.pay)
    TextView pay;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.zhishi)
    TextView zhishi;

    private String getIntegral = null;
    private int FIX_00 = 0;
    private int FIX_01 = 1;
    private static final int SDK_PAY_FLAG = 1;
    private String orderInfo = null;


    public static final String PARTNER = "";

    // 商户收款账号
    public static final String SELLER = "";

    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_leidou);


        titleName.setText(R.string.str_center_my_leidou);
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            headIv.setImageResource(R.drawable.ic_make_tou_03);
        }else {
            UserData data = GlobalUser.getInstance().getUserData();
            headName.setText(data.getNickname());
            if (!TextUtils.isEmpty(data.getImage())) {
                GlideUtil.load(RetrofitProvider.TOEFLURL + data.getImage(), headIv);
            }
        }
       initDatas();
    }


    private void initDatas() {

        addToCompositeDis(HttpUtil.getLeidou()
                .subscribe(new Consumer<LeidouReData>() {
                    @Override
                    public void accept(@NonNull LeidouReData data) throws Exception {
                        if(!TextUtils.isEmpty(data.getIntegral())){
                            getIntegral = data.getIntegral();
                            leiNum.setText(HtmlUtil.fromHtml(getString(R.string.str_leidou_num, data.getIntegral())));
                        }
                       List<DetailsBean> detail = data.getDetails();
                        initLeiData(detail);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(getIntegral)&& !getIntegral.equals("null")){
                    RechargeActi(MyLeidouActivity.this, getIntegral);
                }else {
                    toastShort("暂时不能充值");
                }

//                forword (RechargeActivity.class);
//                DealActivity.startDealActivity(mContext, "支付充值", "http://login.gmatonline.cn/cn/index?source=20&url=http://order.gmatonline.cn/pay/order/integral");
            }
        });
    }


    private void initLeiData(final List<DetailsBean> data) {//雷豆

            initLeiDatasss(data, FIX_00);
            if(data.size() <= 4){
                little.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                return;
            }

            little.setOnClickListener(new View.OnClickListener() {//收起
                @Override
                public void onClick(View v) {

                    initLeiDatasss(data, FIX_00);
                    little.setVisibility(View.GONE);
                    more.setVisibility(View.VISIBLE);
                }
            });

            more.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    initLeiDatasss(data,FIX_01);
                    little.setVisibility(View.VISIBLE);
                    more.setVisibility(View.GONE);
                }
            });

    }


    private void initLeiDatasss(final List<DetailsBean> data1, int index){

        leiRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if(!data1.toString().equals("null")  && !TextUtils.isEmpty(data1.toString())){

            LeidouReAdapter leiAdapter = new LeidouReAdapter(this, data1, index);
//        leiRecycler.addItemDecoration(new RecycleViewLinearDivider(this, LinearLayoutManager.VERTICAL, R.drawable.whitle_fifteen_divider));
            leiRecycler.setAdapter(leiAdapter);

        }else {
            relative.setVisibility(View.GONE);
            zhishi.setVisibility(View.VISIBLE);
        }

    }
}
