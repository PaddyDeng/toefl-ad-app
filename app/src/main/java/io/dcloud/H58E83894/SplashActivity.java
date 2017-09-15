package io.dcloud.H58E83894;

import android.os.Bundle;
import android.text.TextUtils;

import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.AdvertisingData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.guide.GuideActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxHelper;
import io.dcloud.H58E83894.utils.SharedPref;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {
    private static final int TIME_LOAD_MIN = 2000;
    private static final int TIME_LOAD_MAX = 10;
    private long loadTime = 0, startTime;
    private AdvertisingData advertisingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        jump(TIME_LOAD_MAX, true);
    }

    protected void jump(int time, final boolean jumpMain) {
        addToCompositeDis(RxHelper.delay(time).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (shouldGoGuide())
                    goGuide();
                else {
                    if (jumpMain)
                        goMain();
                    else
                        goAdv();

                }
            }
        }));
    }

    private void startMainAct() {
        loadTime = System.currentTimeMillis() - startTime;
        if (TIME_LOAD_MIN > loadTime) {
            int l = (int) ((TIME_LOAD_MAX - loadTime) / 1000);
            jump(l <= 0 ? 1 : 0, true);
        } else {
            goMain();
        }
    }

    private void goAdv() {
        if (advertisingData == null) {
            goMain();
        } else {
            AdvertisingActivity.setAdvert(mContext, advertisingData.getImage(), advertisingData.getTime(), advertisingData.getUrl());
            finishWithAnim();
        }
    }

    private void goMain() {
        forword(MainActivity.class);
        finishWithAnim();
    }

    private void startAdvertising() {
        loadTime = System.currentTimeMillis() - startTime;
        if (TIME_LOAD_MIN > loadTime) {
            int l = (int) ((TIME_LOAD_MAX - loadTime) / 1000);
            jump(l <= 0 ? 1 : l, false);
        } else {
            goAdv();
        }
    }

    private void goGuide() {
        forword(GuideActivity.class);
        finishWithAnim();
    }

    private void goGuideAct() {
        loadTime = System.currentTimeMillis() - startTime;
        if (TIME_LOAD_MIN > loadTime) {
            int l = (int) ((TIME_LOAD_MAX - loadTime) / 1000);
            jump(l <= 0 ? 1 : l, false);
        } else {
            goGuide();
        }
    }

    @Override
    protected void initData() {
        startTime = System.currentTimeMillis();
        login(new RequestImp() {
            @Override
            public void requestSuccess(Object o) {
                firstEnterOrGoAdv();
            }

            @Override
            public void requestFail(String msg) {
                if (TextUtils.equals(C.ACCOUNT_PASSWORD_ISEMPTY, msg)) {
                    firstEnterOrGoAdv();
                } else {
                    startMainAct();
                }
            }
        });
    }

    private boolean shouldGoGuide() {
        return SharedPref.getGuideInfo(mContext) < C.CONT_GUIDE_IMG;
    }

    private void firstEnterOrGoAdv() {
        if (shouldGoGuide()) {
            //去引导页
            goGuideAct();
        } else {
            asynAdvertising();
        }
    }

    private void asynAdvertising() {
        addToCompositeDis(HttpUtil
                .getAdvertisingInfo()
                .subscribe(new Consumer<AdvertisingData>() {
                    @Override
                    public void accept(@NonNull AdvertisingData data) throws Exception {
                        advertisingData = data;
                        judge();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void judge() {
        if (advertisingData.isJudge()) {
            //去广告页
            startAdvertising();
        } else {
            //去首页
            startMainAct();
        }
    }
}
