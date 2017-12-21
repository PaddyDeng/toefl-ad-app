package io.dcloud.H58E83894;

import android.app.Application;
import android.content.Context;

import com.caimuhao.rxpicker.RxPicker;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import io.dcloud.H58E83894.ui.toeflcircle.load.GlideImageLoader;
<<<<<<< HEAD
=======
import io.dcloud.H58E83894.utils.C;
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb

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

<<<<<<< HEAD

=======
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
<<<<<<< HEAD
        Stetho.initializeWithDefaults(this);
        RxPicker.init(new GlideImageLoader());
        refWatcher = LeakCanary.install(this);
        CrashReport.initCrashReport(getApplicationContext(), "f4c9f77412", false);
=======
        RxPicker.init(new GlideImageLoader());
        refWatcher = LeakCanary.install(this);

        if (BuildConfig.appType == C.APP_TYPE_DEV)
            Stetho.initializeWithDefaults(this);
        else
            CrashReport.initCrashReport(getApplicationContext(), "f4c9f77412", false);

>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    }

}
