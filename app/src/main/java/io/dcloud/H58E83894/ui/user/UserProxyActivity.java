package io.dcloud.H58E83894.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.utils.RegexValidateUtil;
import io.reactivex.Observable;

public class UserProxyActivity extends BaseActivity implements OnUserInfoListener {

    private static final int CONTAINER = R.id.login_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .add(CONTAINER, new LoginFragment())
                    .commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
                return super.onKeyDown(keyCode, event);
            } else {
                getSupportFragmentManager().popBackStack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
            leftBack(null);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(CONTAINER, fragment)
                .addToBackStack(tag)
                .commitAllowingStateLoss();
    }

    @Override
    public void replaceActivity(Activity activity, String tag) {

    }

    @Override
    public Observable<UserInfo> login(String account, String password) {
        return HttpUtil.login(account, password);
    }

    @Override
    public void finishActivity() {
        finishWithAnim();
    }

    @Override
    public void asyncAuthCode(String account, String type, RequestImp requestImp) {
        if (RegexValidateUtil.checkPhoneNumber(account)) {
            HttpUtil.numGetAuthCode(account, type).subscribe(new ResultObserver(requestImp));
        } else if (RegexValidateUtil.checkEmail(account)) {
            HttpUtil.emailGetAuthCode(account, type).subscribe(new ResultObserver(requestImp));
        } else {
            toastShort(R.string.str_account_error);
        }
    }

}
