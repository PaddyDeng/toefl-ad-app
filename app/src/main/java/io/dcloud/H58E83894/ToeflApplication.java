package io.dcloud.H58E83894;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.caimuhao.rxpicker.RxPicker;
import com.facebook.stetho.Stetho;
import com.mob.MobSDK;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.ui.toeflcircle.load.GlideImageLoader;
import io.dcloud.H58E83894.utils.ShareProxy;
import io.dcloud.H58E83894.utils.Sharedprefence;


public class ToeflApplication extends Application {

    private static Context mContext;
    private RefWatcher refWatcher;
    public static ToeflApplication app;

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
        mContext = this;
        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        ShareProxy.getInstance();
//        MobSDK.init(this, "8604ac4f7a572584ddd0b008ef1ce5f7");
        MobSDK.init(this,"1a62699572f02","268503fe94ab047ba5b7136738a13154");
        Stetho.initializeWithDefaults(this);
        RxPicker.init(new GlideImageLoader());
        refWatcher = LeakCanary.install(this);
        CrashReport.initCrashReport(getApplicationContext(), "f4c9f77412", false);
    }

    public static RefWatcher getRefWatcher(Context context) {
        ToeflApplication application = (ToeflApplication) context.getApplicationContext();
        return application.refWatcher;
    }
    /**
     * 数据存储到本地数据库
     *
     * @param key
     * @param value
     * @return void
     */
    public void setData(String key, String value) {
        Sharedprefence.WriteSharedPreferences("Cache", key, value);
    }

//
//    public void setData(String key, Boolean value) {
//        Sharedprefence.WriteSharedPreferences("Cache", key, value);
//    }
//
//    public boolean getData(String key , boolean v) {
//        return Sharedprefence.getValueByName("Cache", key, Sharedprefence.BOOLEAN);
//    }
    /**
     * 取出本地数据
     *
     * @param key
     * @return String
     */
    public String getData(String key) {
        return Sharedprefence.getValueByName("Cache", key, Sharedprefence.STRING).toString();
    }

    /**
     * 删除一条本地数据
     *
     * @param key
     * @return String
     */
    public void deleteData(String key) {
        Sharedprefence.removeSharedPreferences("Cache", key);
    }

    /**
     * 写入对象
     *
     * @param key
     * @param o
     */
    public void saveObjData(String key, Object o) {
        Sharedprefence.saveObject("Cache", key, o);
    }

    /**
     * 读取对象
     *
     * @param key
     */
    public Object readObjData(String key) {
        return Sharedprefence.readObject("Cache", key);
    }

    /**
     * 删除本地数据文件
     *
     * @return String
     */
    public void clearDatabase(String dataBaseName) {
        Sharedprefence.ClearSharedPreferences(dataBaseName);
    }


    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            restartApp();//发生崩溃异常时,重启应用
        }
    };
    public void restartApp(){

        GlobalUser.getInstance().exitLogin(mContext);
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }
}
