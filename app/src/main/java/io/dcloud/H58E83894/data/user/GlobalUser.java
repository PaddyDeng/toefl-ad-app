package io.dcloud.H58E83894.data.user;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.JsonUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.SharedPref;

public class GlobalUser {
    private static final String TAG = "GlobalUser";
    private static GlobalUser instance;
    private UserData userData;

    private GlobalUser() {
        //希望初始化的时候就初始化userInfo
        if (null == userData)
            resetUserDataBySharedPref(SharedPref.getLoginInfo(ToeflApplication.getInstance()));
        if (null == userData)
            userData = new UserData();
    }

    public static GlobalUser getInstance() {
        if (instance == null) {
            synchronized (GlobalUser.class) {
                if (instance == null) {
                    instance = new GlobalUser();
                }
            }
        }
        return instance;
    }

    public UserData getUserData() {
        if (isAccountDataInvalid()) {
            // 同步，避免线程被关闭后，多处调用，多次执行重置
            synchronized (GlobalUser.class) {
                if (isAccountDataInvalid()) {
                    resetUserDataBySharedPref(SharedPref.getLoginInfo(ToeflApplication.getInstance()));
                }
            }
        }
        return userData;
    }

    public void setUserData(UserData bean) {
        this.userData = bean;
    }

    public void setNickName(String name) {
        userData.setNickname(name);
    }


    public void setPhone(String phone) {
        userData.setPhone(phone);
    }

    public void setEmail(String email) {
        userData.setEmail(email);
    }

    public void setPhotoUrl(String url) {
        userData.setImage(url);
    }

    public void resetUserInfoToSp(Context c) {
        SharedPref.saveLoginInfo(c, JsonUtil.toJson(GlobalUser.getInstance().getUserData()));
    }

    public void exitLogin(Context mContext) {
        SharedPref.saveLoginInfo(mContext, "");
        GlobalUser.getInstance().clearData();
        RetrofitProvider.clearCookie();
        RxBus.get().post(C.LOGIN_INFO, false);//false用户退出登录
    }

    /**
     * 账户数据是否失效：被系统回收？
     *
     * @return true 无效
     */
    public boolean isAccountDataInvalid() {
        if (null == userData || 0 == userData.getUid()) {//===================0合法不
//            Utils.logh(TAG, "getUserData invalid, reset GlobalData");
            instance = getInstance();
            return true;
        }
        return false;
    }

    public void setUid(int uid) {
        userData.setUid(uid);
    }

    /**
     * 直接重置账户信息
     *
     * @param info
     */
    public void resetUserDataBySharedPref(String info) {
        if (!TextUtils.isEmpty(info)) {
            setUserData((UserData) JsonUtil.fromJson(info, new TypeToken<UserData>() {
            }.getType()));
        }
    }

    /**
     * 注销登录时清空数据
     */
    public void clearData() {
        if (instance != null) {
            instance = null;
        }
    }
}
