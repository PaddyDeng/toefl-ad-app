package io.dcloud.H58E83894.receiver;

import android.content.IntentFilter;

import io.dcloud.H58E83894.utils.C;


public abstract class RemarkRefreshReceiver extends CustomerRefreshReceiver {
    @Override
    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(C.REMARK_REFRESH_ACTION);
        return intentFilter;
    }

}
