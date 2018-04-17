package io.dcloud.H58E83894.ui.user;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.data.InforData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.TodayData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestCallback;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.information.GradeActivity;
import io.dcloud.H58E83894.ui.user.modify.ModifyNickActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.JsonUtil;
import io.dcloud.H58E83894.utils.RegexValidateUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.SharedPref;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.Response;

import static android.R.attr.data;

/**
 * Created by fire on 2017/7/13.
 */
public abstract class BaseUserFragment extends BaseFragment implements RequestCallback<ResultBean> {

    private String accounts;
    private String passwords;

    @Override
    public void requestFail(String msg) {
        dismissLoadDialog();
        toastShort(msg);
    }

    @Override
    public void requestComplete() {
        //dismissLoadDialog();
        //因为要有其他操作，故不能关闭对话框
    }

    @Override
    public void beforeRequest(Disposable d) {
        showLoadDialog();
        addToCompositeDis(d);
    }

    @Override
    public void requestSuccess(ResultBean bean) {
        toastShort(bean.getMessage());
        if (!getHttpResSuc(bean.getCode())) {
            dismissLoadDialog();
        }
    }

    private Map param = new HashMap();
    private String msg = "";
    private String nickName;

    protected void login(final OnUserInfoListener mOnUserInfoListener, String account, String password) {
        accounts = account;
        passwords = password;
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
        mOnUserInfoListener.login(account, password)
                .flatMap(new Function<UserInfo, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull UserInfo info) throws Exception {
                        msg = info.getMessage();
                        if (getHttpResSuc(info.getCode())) {
                            //登录成功，重置session
                            nickName = info.getNickname();
                            param.put("uid", info.getUid());
                            param.put("username", info.getUsername());
                            param.put("password", info.getPassword());
                            param.put("email", info.getEmail());
                            param.put("phone", info.getPhone());
                            param.put("nickname", nickName);
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
                    }

                    @Override
                    public void requestSuccess(ResultBean<UserData> bean) {
                        toastShort(msg);
                        UserData data = bean.getData();
                        String userJson = JsonUtil.toJson(data);
                        SharedPref.saveLoginInfo(getActivity(), userJson);
                        GlobalUser.getInstance().resetUserDataBySharedPref(userJson);
                        GlobalUser.getInstance().setUserData(data);
                        if (TextUtils.isEmpty(data.getNickname())) {
                            //去设置昵称页面
                            forword(ModifyNickActivity.class);
                            getActivity().finish();
                        } else {
                            if(!TextUtils.isEmpty(accounts.toString()) && !TextUtils.isEmpty(passwords)){
                                addToCompositeDis(HttpUtil //判断是否信息采集过,只在登录的时候进行信息采集
                                        .userInfor()
                                        .subscribe(new Consumer<InforData>() {
                                            @Override
                                            public void accept(@NonNull InforData data) throws Exception {
                                                if (data.getData()) return;
                                                GradeActivity.startGradeActivity(getContext(),  accounts, passwords);
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@NonNull Throwable throwable) throws Exception {

                                            }
                                        }));
                            }
                            //登录成功，关闭页面
                            RxBus.get().post(C.LOGIN_INFO, true);
                            mOnUserInfoListener.finishActivity();
                        }
                    }
                }));
    }

    protected void getAuthCode(OnUserInfoListener mOnUserInfoListener, String account, String type, final AuthCode mAuthCode) {
        if (TextUtils.isEmpty(account)) {
            toastShort(R.string.str_login_account);
            return;
        }
        mOnUserInfoListener.asyncAuthCode(account, type, new RequestImp<ResultBean>() {
            @Override
            public void beforeRequest(Disposable disposable) {
                addToCompositeDis(disposable);
                mAuthCode.start();
            }

            @Override
            public void requestFail(String msg) {
                mAuthCode.sendAgain();
            }

            @Override
            public void requestSuccess(ResultBean bean) {
                if (bean.getCode() == 0) {
                    toastShort(bean.getMessage());
                    mAuthCode.sendAgain();
                }
            }
        });
    }

    /**
     * 返回0表示验证不通过
     */
    protected int checkInputInfo(String account, String authcode, String pwd) {
        int type = 0;
        if (TextUtils.isEmpty(account)) {
            toastShort(R.string.str_login_account);
            return type;
        }
        if (TextUtils.isEmpty(authcode)) {
            toastShort(R.string.str_enter_auth_code);
            return type;
        }
        if (TextUtils.isEmpty(pwd)) {
            toastShort(R.string.str_login_pwd);
            return type;
        }
        if (RegexValidateUtil.checkPhoneNumber(account)) {
            type = 1;
        } else if (RegexValidateUtil.checkEmail(account)) {
            type = 2;
        } else {
            toastShort(R.string.str_account_error);
        }
        return type;
    }
}