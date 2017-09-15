package io.dcloud.H58E83894.http;

import android.util.SparseArray;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import io.dcloud.H58E83894.BuildConfig;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitProvider {
    public static String BASEURL = "http://www.gmatonline.cn/";
    private static String LOGINURL = "http://login.gmatonline.cn/";
    public static String TOEFLURL = "http://www.toeflonline.cn/";
    public static String SMARTAPPLYURL = "http://smartapply.gmatonline.cn/";
    //    public static String GOSSIPURL = "http://gossip.gmatonline.cn/";
    public static String GOSSIPURL = "http://bbs.viplgw.cn/";
    private static String VIPLGW = "http://open.viplgw.cn/";

    private static SparseArray<Retrofit> sparseArray = new SparseArray<>();
    private static SparseArray<CookieManager> cookieArray = new SparseArray<>();

    private RetrofitProvider() {
    }

    public static Retrofit getInstance(@HostType.HostTypeChecker int hostType) {
        Retrofit instance = sparseArray.get(hostType);
        if (instance == null) {
            if (instance == null) {
                instance = SingletonHolder.create(hostType);
                sparseArray.put(hostType, instance);
            }
        }
        return instance;
    }

    private static class SingletonHolder {

        private static Retrofit create(@HostType.HostTypeChecker int type) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(20, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }

            String url = null;
            switch (type) {
                case HostType.LOGIN_REGIST_HOST:
                    url = LOGINURL;
                    break;
                case HostType.BASE_URL_HOST:
                    url = BASEURL;
                    break;
                case HostType.TOEFL_URL_HOST:
                    url = TOEFLURL;
                    break;
                case HostType.GOSSIP_URL_HOST:
                    url = GOSSIPURL;
                    break;
                case HostType.SMARTAPPLY_URL_HOST:
                    url = SMARTAPPLYURL;
                    break;
                case HostType.VIPLGW_URL_HOST:
                    url = VIPLGW;
                    break;
            }
            CookieManager cookieManager = cookieArray.get(type);
            if (cookieManager == null) {
                cookieManager = new CookieManager();
                cookieArray.put(type, cookieManager);
            }
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            builder.cookieJar(new JavaNetCookieJar(cookieManager));

            builder.networkInterceptors().add(new StethoInterceptor());
//            builder.addNetworkInterceptor(new AutoLoginInterceptor());

            return new Retrofit.Builder()
                    .baseUrl(url)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    public static void clearCookie() {
        for (int i = 0, size = cookieArray.size(); i < size; i++) {
            CookieManager manager = cookieArray.get(cookieArray.keyAt(i));
            manager.getCookieStore().removeAll();
        }
    }

}
