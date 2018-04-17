package io.dcloud.H58E83894.push;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.utils.Utils;

public class PushProxy {

    private PushProxy() {
    }

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final String TAG = "PushProxy";
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(ToeflApplication.getInstance(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
//                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Utils.logh(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };


    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag or alias success";
                    Utils.logh(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Utils.logh(TAG, logs);
                    if (isConnected(ToeflApplication.getInstance())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Utils.logh(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Utils.logh(TAG, logs);
            }

        }

    };


    public static void stopPush() {
        JPushInterface.stopPush(ToeflApplication.getInstance());
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }


    public static void setAlias(String uid) {
        uid = "lgw" + uid;
        if (JPushInterface.isPushStopped(ToeflApplication.getInstance())) {
            JPushInterface.resumePush(ToeflApplication.getInstance());
        }
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        if (!isValidTagAndAlias(uid)) {//格式不正确
            return;
        }
        Utils.logh(TAG, "setAlias: " + uid);
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, uid));
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    private static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
