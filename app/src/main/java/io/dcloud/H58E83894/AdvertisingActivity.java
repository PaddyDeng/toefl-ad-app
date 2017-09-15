package io.dcloud.H58E83894;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.RxHelper;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class AdvertisingActivity extends BaseActivity {
    private static final String IMG_URL = "img_url";
    private static final String TIMES = "time";
    private static final String JUMP_URL = "jump_url";

    private String imgUrl;
    private String jumpUrl;
    private int time;

    public static void setAdvert(Context context, String imgUrl, int time, String jumpUrl) {
        Intent intent = new Intent(context, AdvertisingActivity.class);
        intent.putExtra(IMG_URL, imgUrl);
        intent.putExtra(TIMES, time);
        intent.putExtra(JUMP_URL, jumpUrl);
        context.startActivity(intent);
    }

    @BindView(R.id.advertising_img)
    ImageView adImg;
    @BindView(R.id.advertising_counter_down)
    TextView counterDownTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
    }

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        imgUrl = intent.getStringExtra(IMG_URL);
        jumpUrl = intent.getStringExtra(JUMP_URL);
        time = intent.getIntExtra(TIMES, 5);//默认5秒
    }

    @Override
    protected void initView() {
        if (!TextUtils.isEmpty(imgUrl))
            imgUrl = RetrofitProvider.TOEFLURL + imgUrl;
        GlideUtil.loadNoDefalut(imgUrl, adImg);
        counterDownTv.setText(getString(R.string.str_counter_down, time));
        addToCompositeDis(RxHelper
                .countDown(time)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        counterDownTv.setText(getString(R.string.str_counter_down, integer));
                        if (integer.intValue() <= 0) {
                            goMain();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        goMain();
                    }
                }));
    }

    @OnClick({R.id.advertising_counter_down, R.id.advertising_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.advertising_counter_down:
                goMain();
                break;
            case R.id.advertising_img:
                goDealActivity();
                break;
            default:
                break;
        }
    }

    private void goDealActivity() {
        if (!TextUtils.isEmpty(jumpUrl)) {
            DealActivity.startDealActivity(mContext, "", jumpUrl, C.DEAL_GO_MAIN);
            finishWithAnim();
        }
    }

    private void goMain() {
        forword(MainActivity.class);
        finishWithAnim();
    }

}
