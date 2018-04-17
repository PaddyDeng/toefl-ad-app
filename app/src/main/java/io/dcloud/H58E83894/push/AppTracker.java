package io.dcloud.H58E83894.push;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import io.dcloud.H58E83894.utils.Utils;


public class AppTracker {
	private static AppTracker instance;
	private ActivityLifecycleCallbacks lifecycleCallbacks;
	private int allActivity;
	private boolean isRunnForground;
	private AppTracker(){
		log("create AppStatusTracker instance .");
		if(lifecycleCallbacks==null){log("create AppStatusTracker instance then create lifecycleCallbacks.");
			lifecycleCallbacks=new ActivityLifecycleCallbacks() {

				@Override
				public void onActivityStopped(Activity activity) {
					allActivity--;
					log("onActivityStopped() allActivity:"+allActivity);
					if(allActivity==0){
						isRunnForground=false;
					}
				}

				@Override
				public void onActivityStarted(Activity activity) {
					allActivity++;
					log("onActivityStarted() allActivity:"+allActivity);
				}

				@Override
				public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
					log("onActivitySaveInstanceState()");
				}

				@Override
				public void onActivityResumed(Activity activity) {
					log("onActivityResumed() ==>current:allActivity:"+allActivity);
					isRunnForground=true;
				}

				@Override
				public void onActivityPaused(Activity activity) {
					log("onActivityPaused()");
				}

				@Override
				public void onActivityDestroyed(Activity activity) {
					log("onActivityDestroyed()");
				}

				@Override
				public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
					log("onActivityCreated()");
				}
			};
		}
	}
	public static AppTracker getInstance(){
		if(null==instance){
			synchronized (AppTracker.class) {
				if(null==instance) instance=new AppTracker();
			}
		}
		return instance;
	}
	public ActivityLifecycleCallbacks getActivityLifecycleCallbacks (){
		return this.lifecycleCallbacks;
	}
	public boolean isAppRunForground(){
		log("isAppRunForground allActivity:"+allActivity);
		return isRunnForground;
	}
	private void log(String msg) {
		Utils.logh("AppStatusTracker", msg);
	}
}
