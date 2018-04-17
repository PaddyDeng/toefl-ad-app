package io.dcloud.H58E83894.receiver;

import android.content.IntentFilter;

import io.dcloud.H58E83894.utils.C;


public abstract class RefreshMakeRecordReceiver extends CustomerRefreshReceiver {
    @Override
    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(C.REFRESH_MAKE_RECORD);
        return intentFilter;
    }
}
