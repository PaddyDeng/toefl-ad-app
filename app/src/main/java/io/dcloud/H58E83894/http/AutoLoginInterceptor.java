package io.dcloud.H58E83894.http;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.ui.user.UserProxyActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.JsonUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by fire on 2017/8/30  11:28.
 */

public class AutoLoginInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originResponse = chain.proceed(request);
        if (loginExpired(originResponse)) {
            autoLogin();
            Request newRequest = generateNewResquest(chain);
            chain.proceed(newRequest);
            return chain.proceed(newRequest);
        }
        return originResponse;
    }

    private void autoLogin() {

    }

    private Request generateNewResquest(Chain chain) {
        return chain.request().newBuilder().build();
    }

    /**
     * 登录过期
     */
    private boolean loginExpired(Response originResponse) throws IOException {
        if (originResponse == null || !originResponse.isSuccessful()) {
            return false;
        }
        ResponseBody responseBody = originResponse.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        Charset charset = UTF_8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset();
        }
        String result = "";
        if (contentLength != 0) {
            result = buffer.clone().readString(charset);
        }
        if (result.contains("code")) {
            AutoLoginData autoLoginData = JsonUtil.fromJson(result, new TypeToken<AutoLoginData>() {
            }.getType());
            if (!Utils.getHttpMsgSu(autoLoginData.getCode())) {
                String message = autoLoginData.getMessage();
                if (TextUtils.isEmpty(message)) return false;
                String account = SharedPref.getAccount(ToeflApplication.getInstance());
                String password = SharedPref.getPassword(ToeflApplication.getInstance());

                if ((TextUtils.equals(message, "登录") || TextUtils.equals(message, "登陆") &&
                        !TextUtils.isEmpty(account) && !TextUtils.isEmpty(password))) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void log(String msg) {
        Utils.logh("AutoLoginInterceptor==>", msg);
    }
}
