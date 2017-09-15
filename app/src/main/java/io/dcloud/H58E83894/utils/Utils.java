package io.dcloud.H58E83894.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.dcloud.H58E83894.BuildConfig;
import io.dcloud.H58E83894.ToeflApplication;
import retrofit2.HttpException;

import static android.widget.Toast.makeText;

public class Utils {

    public static boolean LOG_H = false;

    static {
        switch (BuildConfig.appType) {
            case 1:
                LOG_H = true;
                break;
            case 2:
                LOG_H = false;
                break;
            default:
                LOG_H = false;
                break;
        }
    }

    public static void logh(String tag, String msg) {
        if (LOG_H) {
            Log.d(tag, msg);
        }
    }

    public static boolean isEmpty(String... key) {
        String[] str = key;
        for (int i = 0, size = str.length; i < size; i++) {
            if (TextUtils.isEmpty(str[i])) {
                return true;
            }
        }
        return false;
    }

    public static void removeOnGlobleListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int splitInt(String str) {
        if (TextUtils.isEmpty(str))
            return 0;
        String s = str.replaceAll("\\D", "");
        return Integer.parseInt(s);
    }

    public static boolean isMultChosse(String type) {
        if (TextUtils.equals(C.MULT_CHOOSE, type)) {
            return true;
        }
        return false;
    }

    public static String format(int time) {
        //以秒为单位
        StringBuffer sb = new StringBuffer();
        int hour = time / 3600;
        int newTime = time % 3600;
        if (hour > 0) {
            sb.append(hour);
            sb.append("h");
        }
        int min = newTime / 60;
        int sec = newTime % 60;
        if (min > 0) {
            sb.append(min);
            sb.append("m");
        }
        sb.append(sec);
        sb.append("s");
        return sb.toString();
    }

    public static boolean isBelowAndroidVersion(int version) {
        return Build.VERSION.SDK_INT < version;
    }

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String onError(Throwable throwable) {
        String errorMsg = "";
        if (throwable instanceof HttpException) {
            switch (((HttpException) throwable).code()) {
                case 403:
                    errorMsg = "没有权限访问此链接！";
                    break;
                case 504:
                    if (isConnected(ToeflApplication.getInstance())) {
                        errorMsg = "网络连接超时！";
                    } else {
                        errorMsg = "没有联网哦！";
                    }
                    break;
                default:
                    errorMsg = ((HttpException) throwable).message();
                    break;
            }
        } else if (throwable instanceof UnknownHostException) {
            if (isConnected(ToeflApplication.getInstance())) {
                errorMsg = "网络连接超时！";
            } else {
                errorMsg = "没有联网哦！";
            }
        } else if (throwable instanceof SocketTimeoutException) {
            errorMsg = "网络连接超时！";
        } else {
            errorMsg = throwable.getMessage();
        }
        return errorMsg;
    }

    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    public static List<String> splitStr(String questionsid, String format) {
        String[] ids = questionsid.split(format);
        List<String> arr = new ArrayList<>();
        if (ids != null || ids.length > 0) {
            for (String id : ids) {
                if (TextUtils.isEmpty(id)) continue;
                arr.add(id);
            }
        }
        return arr;
    }

    public static String getCurrentVersion(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCurrentVersionNum(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void callPhone(Activity context, String num) {
        if (TextUtils.isEmpty(num)) return;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + num));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Utils.toastShort(context, "没有拨打电话应用");
        }
    }

    public static List<String> splitStrToList(String questionsid) {
        String[] ids = questionsid.split(",");
        List<String> arr = new ArrayList<>();
        if (ids != null || ids.length > 0) {
            for (String id : ids) {
                arr.add(id);
            }
        }
        return arr;
    }

    public static String getEditTextString(EditText et) {
        Editable text = et.getText();
        return text != null && text.toString().trim().length() != 0 ? text.toString().trim() : null;
    }

    public static void toastShort(Context context, int id) {
        makeText(context, context.getString(id), Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String msg) {
        makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String msg, int time) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setDuration(time);
        toast.show();
    }

    public static boolean getHttpMsgSu(int code) {
        if (code == C.REQUEST_RESULT_SUCCESS) {
            return true;
        }
        return false;
    }

    public static void setGone(View... views) {
        if (views != null && views.length > 0) {
            View[] v = views;
            int size = views.length;

            for (int i = 0; i < size; ++i) {
                View view = v[i];
                if (view != null && view.getVisibility() != View.GONE) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    public static void setVisible(View... views) {
        if (views != null && views.length > 0) {
            View[] v = views;
            int size = views.length;

            for (int i = 0; i < size; ++i) {
                View view = v[i];
                if (view != null && view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public static void setInvisible(View... views) {
        if (views != null && views.length > 0) {
            View[] var4 = views;
            int var3 = views.length;

            for (int var2 = 0; var2 < var3; ++var2) {
                View view = var4[var2];
                if (view != null && view.getVisibility() != View.INVISIBLE) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }

    }


    public static String split(String url) {
        if (TextUtils.isEmpty(url))
            return "";
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        return url;
    }


    public static void keyBordHideFromWindow(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    public static void keyBordShowFromWindow(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 格式化后数据：  00:00
     */
    public static String formatTime(long time) {
        StringBuffer sb = new StringBuffer();
        long as = time / 1000;
        as += time % 1000 > 0 ? 1 : 0;
        long min = as / 60;
        long s = as % 60;
        sb.append(addZero(min));
        sb.append(":");
        sb.append(addZero(s));
        return sb.toString();
    }

    private static String addZero(long time) {
        if (time < 10) {
            return TextUtils.concat("0", String.valueOf(time)).toString();
        }
        return String.valueOf(time);
    }

    public static void controlTvFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public static String[] splitOption(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return new String[]{};
        }
//        if (!txt.contains("\\r\\n")) {
//            return new String[]{txt};
//        }
        return txt.split("\\r\\n");
    }

    public static String[] splitOptionThroughN(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return new String[]{};
        }
//        if (!txt.contains("\\r\\n")) {
//            return new String[]{txt};
//        }
        return txt.split("\\n");
    }

    public static String removerepeatedchar(String target) {
        if (TextUtils.isEmpty(target)) return target;
        if (target.length() == 1) return target;
        StringBuilder sb = new StringBuilder();
        List<Character> ls = new ArrayList<>();
        int i = 0, len = target.length();
        while (i < len) {
            char c = target.charAt(i);
            i++;
            if (ls.contains(c))
                continue;
            ls.add(c);
        }
        for (Character c : ls) {
            sb.append(c);
        }
        return sb.toString();
    }

}
