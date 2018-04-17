package io.dcloud.H58E83894.ui.user;

import android.app.Activity;
import android.support.v4.app.Fragment;

import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.reactivex.Observable;

/**
 * Created by fire on 2017/7/13.
 */

public interface OnUserInfoListener {
    void onBack();

    void replaceFragment(Fragment fragment, String tag);
    void replaceActivity(Activity activity, String tag);

    Observable<UserInfo> login(String account, String password);

    void finishActivity();

    void asyncAuthCode(String account, String type, RequestImp requestImp);
}
