package io.dcloud.H58E83894.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/13.
 */

public class RetrievePswFragment extends BaseUserFragment {

    private AuthCode mAuthCode;
    private OnUserInfoListener mOnUserInfoListener;
    @BindView(R.id.retrieve_auth_code)
    TextView authTv;
    @BindView(R.id.retrieve_input_account)
    EditText inputAccount;
    @BindView(R.id.retrieve_input_auth_code)
    EditText inputAuthCode;
    @BindView(R.id.retrieve_input_new_password)
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
        //初始化短信
        addToCompositeDis(HttpUtil.phone_request().subscribe(new Consumer<JsonObject>() {
            @Override
            public void accept(@NonNull JsonObject jsonObject) throws Exception {
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAuthCode != null)
            mAuthCode.destory();
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_retrieve_layout, container, false);
    }

    @OnClick({R.id.retrieve_auth_code, R.id.retrieve_left_back, R.id.retrieve_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retrieve_auth_code:
                getAuthCode(mOnUserInfoListener, getEditText(inputAccount), C.RETRIEVE_TYPE, mAuthCode);
                break;
            case R.id.retrieve_left_back:
                Utils.keyBordHideFromWindow(getActivity(), authTv);
                mOnUserInfoListener.onBack();
                break;
            case R.id.retrieve_sure:
                retrievePwd();
                break;
            default:
                break;
        }
    }

    private void retrievePwd() {
        String account = getEditText(inputAccount);
        String authcode = getEditText(inputAuthCode);
        String pwd = getEditText(inputPwd);
        int type = checkInputInfo(account, authcode, pwd);
        HttpUtil.retrievePwd(String.valueOf(type), account, pwd, authcode).subscribe(new ResultObserver(this));
    }

    @Override
    public void requestSuccess(ResultBean bean) {
        super.requestSuccess(bean);
        if (getHttpResSuc(bean.getCode())) {//找回密码成功，去登录
            login(mOnUserInfoListener, getEditText(inputAccount), getEditText(inputPwd));
        }
    }
}
