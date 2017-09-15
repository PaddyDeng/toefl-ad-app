package io.dcloud.H58E83894.ui.center.setting.modify;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestCallback;
import io.dcloud.H58E83894.ui.common.BaseDialog;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by fire on 2017/7/17  16:11.
 */

public abstract class BaseModifyDialog extends BaseDialog {

    protected Map param = new HashMap();

    protected void login(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            toastShort(R.string.str_login_account);
            return;
        }
        if (password.length() < 5 || password.length() > 16) {
            toastShort(R.string.str_enter_your_pwd);
            return;
        }
        SharedPref.saveAccount(getActivity(), account);
        SharedPref.savePassword(getActivity(), password);
        HttpUtil.login(account, password)
                .flatMap(new Function<UserInfo, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull UserInfo info) throws Exception {
                        if (Utils.getHttpMsgSu(info.getCode())) {
                            //登录成功，重置session
                            param.put("uid", info.getUid());
                            param.put("username", info.getUsername());
                            param.put("password", info.getPassword());
                            param.put("email", info.getEmail());
                            param.put("phone", info.getPhone());
                            param.put("nickname", info.getNickname());
                            return HttpUtil.toefl(param);
                        } else {
                            throw new IllegalArgumentException(info.getMessage());
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.gossip(param);
                        } else {
                            throw new IllegalArgumentException("toefl reset session fail");
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.gmatl(param);
                        } else {
                            throw new IllegalArgumentException("gossip reset session fail");
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.smartapply(param);
                        } else {
                            throw new IllegalArgumentException("gmatl reset session fail");
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<ResultBean<UserData>>>() {
                    @Override
                    public ObservableSource<ResultBean<UserData>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.getUserDetailInfo();
                        } else {
                            throw new IllegalArgumentException("smartapply reset session fail");
                        }
                    }
                })
                .compose(new SchedulerTransformer<ResultBean<UserData>>())
                .subscribe(new ResultObserver<>(new RequestCallback<ResultBean<UserData>>() {
                    @Override
                    public void beforeRequest(Disposable d) {
                        showLoadDialog();
                        addToCompositeDis(d);
                    }

                    @Override
                    public void requestFail(String msg) {
                        dismissLoadDialog();
                        toastShort(msg);
                    }

                    @Override
                    public void requestComplete() {
                        dismissLoadDialog();
                        dismiss();
                    }

                    @Override
                    public void requestSuccess(ResultBean<UserData> bean) {
                    }
                }));
    }

}
