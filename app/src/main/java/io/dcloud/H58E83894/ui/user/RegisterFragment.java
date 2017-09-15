package io.dcloud.H58E83894.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.Utils;

/**
 * Created by fire on 2017/7/13.
 */

public class RegisterFragment extends BaseUserFragment {

    private AuthCode mAuthCode;
    private OnUserInfoListener mOnUserInfoListener;
    @BindView(R.id.register_auth_code)
    TextView authTv;
    @BindView(R.id.register_account)
    EditText inputAccount;
    @BindView(R.id.register_input_auth_code)
    EditText inputAuthCode;
    @BindView(R.id.register_input_password)
    EditText inputPwd;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnUserInfoListener = (OnUserInfoListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().getName()
                    + " must implement OnUserInfoListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuthCode = new AuthCode(60 * 1000, 1000, getActivity(), authTv);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAuthCode != null)
            mAuthCode.destory();
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_registe_layout, container, false);
    }

    @OnClick({R.id.left_back, R.id.register, R.id.no_register_go_login, R.id.register_auth_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_back:
            case R.id.no_register_go_login:
                Utils.keyBordHideFromWindow(getActivity(), authTv);
                mOnUserInfoListener.onBack();
                break;
            case R.id.register_auth_code:
                getAuthCode(mOnUserInfoListener, getEditText(inputAccount), C.REGISTER_TYPE, mAuthCode);
                break;
            case R.id.register:
                register();
                break;
            default:
                break;
        }
    }

    private void register() {
        String account = getEditText(inputAccount);
        String authcode = getEditText(inputAuthCode);
        String pwd = getEditText(inputPwd);
        int type = checkInputInfo(account, authcode, pwd);
        if (type == 0) return;
        HttpUtil.register(String.valueOf(type), account, pwd, authcode).subscribe(new ResultObserver(this));
    }

    @Override
    public void requestSuccess(ResultBean bean) {
        super.requestSuccess(bean);
        if (getHttpResSuc(bean.getCode())) {//注册成功，去登录
            login(mOnUserInfoListener, getEditText(inputAccount), getEditText(inputPwd));
        }
    }
}
