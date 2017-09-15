package io.dcloud.H58E83894.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.utils.SharedPref;

/**
 * Created by fire on 2017/7/13.
 */
public class LoginFragment extends BaseUserFragment {

    private OnUserInfoListener mOnUserInfoListener;
    @BindView(R.id.login_account)
    EditText inputAccount;
    @BindView(R.id.login_pwd)
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
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_login_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        String account = SharedPref.getAccount(getActivity());
        inputAccount.setText(account);
        inputAccount.setSelection(account.length());
        inputPwd.setText(SharedPref.getPassword(getActivity()));
    }

    @OnClick({R.id.immediately_regist, R.id.login, R.id.forgive_pwd, R.id.exit_no_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.immediately_regist:
                mOnUserInfoListener.replaceFragment(new RegisterFragment(), RegisterFragment.class.getSimpleName());
                break;
            case R.id.forgive_pwd:
                mOnUserInfoListener.replaceFragment(new RetrievePswFragment(), RetrievePswFragment.class.getSimpleName());
                break;
            case R.id.login:
                login(mOnUserInfoListener, getEditText(inputAccount), getEditText(inputPwd));
                break;
            case R.id.exit_no_login:
                mOnUserInfoListener.finishActivity();
                break;
            default:
                break;
        }
    }
}
