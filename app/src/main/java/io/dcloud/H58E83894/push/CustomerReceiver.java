package io.dcloud.H58E83894.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.data.pushdata.PushData;
import io.dcloud.H58E83894.data.pushdata.PushMsgData;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.JsonUtil;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class CustomerReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Bundle bundle = intent.getExtras();
            Utils.logh(TAG, "[CustomerReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Utils.logh(TAG, "[CustomerReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//                Utils.logh(TAG, "[CustomerReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                //{"msg_content":"发帖成功","extras":{"message":"发帖","type":1}}
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Utils.logh(TAG, "[CustomerReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//                receivingNotification(context, bundle);
                Utils.logh(TAG, "[CustomerReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//                JPushInterface.setDefaultPushNotificationBuilder();
//
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Utils.logh(TAG, "[CustomerReceiver] 用户点击打开了通知");
                Bundle extras = intent.getExtras();
                if (extras == null) return;
                String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (TextUtils.isEmpty(type)) return;
                PushData data = JsonUtil.fromJson(type, new TypeToken<PushData>() {
                }.getType());
                if (data != null) {
                    handlePushMsg(context, data, intent);
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Utils.logh(TAG, "[CustomerReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Utils.logh(TAG, "[CustomerReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Utils.logh(TAG, "[CustomerReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    private void handlePushMsg(Context context, PushData data, Intent intent) {
//      {"message":"点赞成功","type":2,"informType":2}
        if (data.getInformType().equals(C.REPLY)) {
            //去到列表页，并显示回复数
            SharedPref.saveOrder(context, data.getInformType());
        }
        //去到首页
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Utils.logh(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Utils.logh(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Utils.logh(TAG, "extras : " + extras);
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Utils.logh(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Utils.logh(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (true) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            PushMsgData msgData = JsonUtil.fromJson(extras, new TypeToken<PushMsgData>() {
            }.getType());
            if (msgData.getType() == 1) {
                int newStatus = SharedPref.getNewStatus(context);
                SharedPref.saveNewStatus(context, ++newStatus);
            }
            Utils.logh(TAG, extras);//{"message":"发帖","type":1}自定义消息格式
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!TextUtils.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
////						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }

//            }
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }
}
