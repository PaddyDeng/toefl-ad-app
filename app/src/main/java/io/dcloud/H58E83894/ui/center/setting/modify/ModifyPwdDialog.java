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
import io.dcloud.H58E83894.utils.SharedPref;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ModifyPwdDialog extends BaseModifyDialog {

    private static ICallBack<String> mCallBack;

    public static ModifyPwdDialog getInstance(ICallBack<String> callBack) {
        ModifyPwdDialog simpleEditDialog = new ModifyPwdDialog();
        simpleEditDialog.mCallBack = callBack;
        return simpleEditDialog;
    }

    @BindView(R.id.modify_old_pwd)
    EditText oldPwd;
    @BindView(R.id.modify_new_pwd)
    EditText newPwd;
    @BindView(R.id.dialog_simple_btn_confirm)
    TextView confirm;
    @BindView(R.id.dialog_simple_btn_cancel)
    TextView cancel;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null)
            mCallBack = null;
    }

    @Override
    protected int getContentViewLayId() {
        return R.layout.modify_pwd_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                if (check(newPwd, oldPwd)) {
                    modifyPwd();
                }
            }
        });
    }

    private void modifyPwd() {
        UserData data = GlobalUser.getInstance().getUserData();
        showLoadDialog();
        addToCompositeDis(HttpUtil.modifyPwd(String.valueOf(data.getUid()), getEditTxt(oldPwd), getEditTxt(newPwd))
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull final ResultBean bean) throws Exception {
                        String newPwdStr = getEditTxt(newPwd);
                        if (getHttpCodeSucc(bean.getCode())) {
                            if (mCallBack != null) {
                                mCallBack.onSuccess(newPwdStr);
                                mCallBack = null;
                            }
                            SharedPref.savePassword(getActivity(), newPwdStr);
                            //需要重新登录
                            login(SharedPref.getAccount(getActivity()), newPwdStr);
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    private boolean check(EditText newPwd, EditText olePwd) {
        String newStr = getEditTxt(newPwd);
        String oldStr = getEditTxt(olePwd);
        if (TextUtils.isEmpty(oldStr)) {
            toastShort(R.string.str_set_modify_old_pwd);
            return false;
        } else if (TextUtils.isEmpty(newStr)) {
            toastShort(R.string.str_set_modify_new_pwd);
            return false;
        } else if (TextUtils.equals(newStr, oldStr)) {
            toastShort(R.string.str_set_modify_pwd_judge);
            return false;
        }
        return true;
    }
}
