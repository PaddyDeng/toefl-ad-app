package io.dcloud.H58E83894.ui.toeflcircle.listener;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class PriaseListener implements View.OnClickListener {
    private BaseActivity baseActivity;
    private RemarkData mRemarkData;

    public PriaseListener(Context context, RemarkData mRemarkData) {
        baseActivity = (BaseActivity) context;
        this.mRemarkData = mRemarkData;
    }

    @Override
    public void onClick(View v) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            Utils.toastShort(baseActivity,R.string.str_no_login_tip);
            return;
        }
        setPriase((TextView) v);
        priaseRemark(v);
    }

    private void priaseRemark(final View v) {
        HttpUtil.praiseOrCancel(mRemarkData.getId()).subscribe(new Consumer<ResultBean>() {
            @Override
            public void accept(@NonNull ResultBean bean) throws Exception {
                if (baseActivity.needAgainLogin(bean)) {
                    //需要重新登录
                    baseActivity.login(new RequestImp() {
                        @Override
                        public void requestSuccess(Object o) {
                            //登录成功
                            HttpUtil.praiseOrCancel(mRemarkData.getId()).subscribe();
                        }

                        @Override
                        public void requestFail(String msg) {
                            setPriase((TextView) v);
                        }
                    });
                } else {
                    //没有过期，不用重新登录
                    if (bean.getCode() == 1 || bean.getCode() == 2) {
                        //1点赞成功 2取消点赞成功
                    } else {
                        setPriase((TextView) v);
                    }
//                }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                setPriase((TextView) v);
            }
        });

    }

    private void setPriase(TextView v) {

        int num = Integer.parseInt(mRemarkData.getLikeNum());
        TextView tv = v;
        if (mRemarkData.isLikeId()) {
            num = num == 0 ? 0 : --num;
            mRemarkData.setLikeId(false);
        } else {
            mRemarkData.setLikeId(true);
            ++num;
        }
        v.setSelected(mRemarkData.isLikeId());
        mRemarkData.setLikeNum(String.valueOf(num));
        tv.setText(String.valueOf(num));
    }

}
