package io.dcloud.H58E83894.ui.center.setting.modify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.user.AuthCode;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RegexValidateUtil;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 修改电话或邮箱后都要重新登录，修改成功，自动就登录了。
 */
public class ModifyPhoneOrEmailDialog extends BaseModifyDialog {

    private static ICallBack<String> mCallBack;
    private static final String KEY_TYPE = "modify_email";

    public static ModifyPhoneOrEmailDialog getInstance(boolean modifyEmail, ICallBack<String> callBack) {
        ModifyPhoneOrEmailDialog simpleEditDialog = new ModifyPhoneOrEmailDialog();
        Bundle b = new Bundle();
        b.putBoolean(KEY_TYPE, modifyEmail);
        simpleEditDialog.setArguments(b);
        simpleEditDialog.mCallBack = callBack;
        return simpleEditDialog;
    }

    @BindView(R.id.modify_dialog_tv_title)
    TextView title;
    @BindView(R.id.modify_acc_et_content)
    EditText et;
    @BindView(R.id.modify_dialog_et_auth)
    EditText etAuthCode;
    @BindView(R.id.modify_authcode)
    TextView authCode;
    @BindView(R.id.dialog_simple_btn_confirm)
    TextView confirm;
    @BindView(R.id.dialog_simple_btn_cancel)
    TextView cancel;
    private AuthCode mAuthCode;
    private boolean modifyEmail;

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle arguments = getArguments();
        if (arguments != null) {
            modifyEmail = arguments.getBoolean(KEY_TYPE);
        }
    }

    @Override
    protected int getContentViewLayId() {
        return R.layout.modify_phone_or_email_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (modifyEmail) {
            title.setText(R.string.str_set_modify_email_title);
            et.setHint(R.string.str_set_email_tip);
        }
        mAuthCode = new AuthCode(60 * 1000, 1000, getActivity(), authCode);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onFail();
                    mCallBack = null;
                }
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(et) && checkAuthCode(etAuthCode)) {
                    modify(et);
                }
            }
        });
        authCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAuthCode();
            }
        });
    }

    private Observable<ResultBean> getAuthCode() {
        if (modifyEmail) {
            return HttpUtil.emailGetAuthCode(getEditTxt(et), C.MODIFY_INFO_TYPE);
        }
        return HttpUtil.numGetAuthCode(getEditTxt(et), C.MODIFY_INFO_TYPE);
    }

    private void sendAuthCode() {
        if (check(et)) {
            mAuthCode.start();
            addToCompositeDis(getAuthCode().subscribe(new Consumer<ResultBean>() {
                @Override
                public void accept(@NonNull ResultBean o) throws Exception {
                    if (o.getCode() != 1) {
                        mAuthCode.sendAgain();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    mAuthCode.sendAgain();
                }
            }));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAuthCode != null)
            mAuthCode.destory();
        if (mCallBack != null)
            mCallBack = null;
    }

    private Observable<ResultBean> getModifyType() {
        UserData data = GlobalUser.getInstance().getUserData();
        if (modifyEmail) {
            return HttpUtil.modifyEmail(String.valueOf(data.getUid()), getEditTxt(et), getEditTxt(etAuthCode));
        }
        return HttpUtil.modifyPhone(String.valueOf(data.getUid()), getEditTxt(et), getEditTxt(etAuthCode));
    }

    private void modify(final EditText et) {
        showLoadDialog();
        addToCompositeDis(getModifyType().doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                        .subscribe(new Consumer<ResultBean>() {
                            @Override
                            public void accept(@NonNull final ResultBean bean) throws Exception {
                                String account = getEditTxt(et);
                                if (getHttpCodeSucc(bean.getCode())) {
                                    if (mCallBack != null) {
                                        mCallBack.onSuccess(account);
                                        mCallBack = null;
                                    }
                                    saveUserInfo(account);
                                    SharedPref.saveAccount(getActivity(), account);
                                    //重新登录
                                    String password = SharedPref.getPassword(getActivity());

                                    login(account, password);
//                                    ReSetSessionProxy.getInstance().login(account, password, new ICallBack() {
//                                        @Override
//                                        public void onSuccess(Object o) {
//
//                                        }
//
//                                        @Override
//                                        public void onFail() {
//                                            dismissLoadDialog();
//                                            dismiss();
//                                        }
//                                    });
                                } else {
                                    toastShort(bean.getMessage());
                                    dismissLoadDialog();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        })
        );
    }

    private void saveUserInfo(String account) {
        if (!TextUtils.isEmpty(account)) {
            if (modifyEmail) {
                GlobalUser.getInstance().setEmail(account);
            } else {
                GlobalUser.getInstance().setPhone(account);
            }
        }
        GlobalUser.getInstance().resetUserInfoToSp(getActivity());
    }


    private boolean checkAuthCode(EditText etAuthCode) {
        String authCode = Utils.getEditTextString(etAuthCode);
        if (TextUtils.isEmpty(authCode)) {
            toastShort(R.string.str_enter_auth_code);
            return false;
        }
        return true;
    }

    private boolean check(final EditText editText) {
        String acc = Utils.getEditTextString(editText);
        if (modifyEmail) {
            if (checkEmail(acc)) return false;
        } else {
            if (checkPhone(acc)) return false;
        }
        return true;
    }

    private boolean checkEmail(String acc) {
        if (TextUtils.isEmpty(acc)) {
            toastShort(R.string.str_set_email_tip);
            return true;
        } else if (!RegexValidateUtil.checkEmail(acc)) {
            toastShort(R.string.str_set_modify_email_format);
            return true;
        } else if (acc.equals(GlobalUser.getInstance().getUserData().getEmail())) {
            toastShort(R.string.str_set_modify_email_com);
            return true;
        }
        return false;
    }

    private boolean checkPhone(String acc) {
        if (TextUtils.isEmpty(acc)) {
            toastShort(R.string.str_set_phone_tip);
            return true;
        } else if (!RegexValidateUtil.checkPhoneNumber(acc)) {
            toastShort(R.string.str_set_modify_phone_format);
            return true;
        } else if (acc.equals(GlobalUser.getInstance().getUserData().getPhone())) {
            toastShort(R.string.str_set_modify_phone_com);
            return true;
        }
        return false;
    }
}
