package io.dcloud.H58E83894;

import android.app.Application;
import android.content.Context;

import com.caimuhao.rxpicker.RxPicker;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import io.dcloud.H58E83894.ui.toeflcircle.load.GlideImageLoader;

public class ToeflApplication extends Application {

    private static Context mContext;
    private RefWatcher refWatcher;

    public static Context getInstance() {
        return mContext;
    }

    public static RefWatcher getRefWatcher(Context context) {
        ToeflApplication application = (ToeflApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Stetho.initializeWithDefaults(this);
        RxPicker.init(new GlideImageLoader());
        refWatcher = LeakCanary.install(this);
        CrashReport.initCrashReport(getApplicationContext(), "f4c9f77412", false);
    }

}
