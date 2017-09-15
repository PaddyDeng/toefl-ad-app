package io.dcloud.H58E83894.http;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class HostType {
    @IntDef({LOGIN_REGIST_HOST, BASE_URL_HOST, TOEFL_URL_HOST, SMARTAPPLY_URL_HOST, GOSSIP_URL_HOST,VIPLGW_URL_HOST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {
    }

    @HostTypeChecker
    public static final int LOGIN_REGIST_HOST = 1;
    @HostType.HostTypeChecker
    public static final int BASE_URL_HOST = 2;
    @HostType.HostTypeChecker
    public static final int TOEFL_URL_HOST = 3;
    @HostType.HostTypeChecker
    public static final int SMARTAPPLY_URL_HOST = 4;
    @HostType.HostTypeChecker
    public static final int GOSSIP_URL_HOST = 5;
    @HostType.HostTypeChecker
    public static final int VIPLGW_URL_HOST = 6;

}
