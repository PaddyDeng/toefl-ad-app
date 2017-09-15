package io.dcloud.H58E83894.ui.common.update;

import android.Manifest;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.setting.VersionInfo;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SimpleUpdateApk implements DownloadApk.OnDownloadApkListener {

    private FragmentActivity mActivity;
    private DownloadApk downloadApk;
    protected CompositeDisposable mCompositeDisposable;
    private boolean initiative;//用户主动更新

    public SimpleUpdateApk(FragmentActivity activity) {
        this(activity, false);//默认不显示最新应用提示
    }

    public SimpleUpdateApk(FragmentActivity mActivity, boolean initiative) {
        this.mActivity = mActivity;
        downloadApk = new DownloadApk(mActivity);
        downloadApk.setOnDownloadApkListener(this);
        mCompositeDisposable = new CompositeDisposable();
        this.initiative = initiative;
    }

    public void checkVersionUpdate() {
        mCompositeDisposable.add(asyncVersionInfo());
    }

    public void onDestory() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        mActivity = null;
        downloadApk = null;
    }

    private Disposable asyncVersionInfo() {
        return HttpUtil.getUpdate()
                .subscribe(new Consumer<VersionInfo>() {
                    @Override
                    public void accept(@NonNull final VersionInfo bean) throws Exception {
                        int versions = 0;
                        if (!TextUtils.isEmpty(bean.getVersion()))
                            versions = Integer.parseInt(bean.getVersion());
                        if (Utils.getCurrentVersionNum(mActivity) < versions) {
                            //弹框提示用户是否需要更新
                            if (bean.isJump())
                                showTipDialog(bean);
                            else if (initiative)
                                showTipDialog(bean);
                        } else {
                            if (initiative) {
                                showToast(R.string.str_is_new_apk);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (initiative)
                            Utils.toastShort(mActivity, Utils.onError(throwable));
                    }
                });
    }

    private void showToast(@StringRes int resId) {
        Utils.toastShort(mActivity, mActivity.getString(resId));
    }

    private void showTipDialog(final VersionInfo info) {
        UpdateApkDialog.getInstance(info.getUpdateInfo(), new ICallBack<String>() {
            @Override
            public void onSuccess(String s) {
                new RxPermissions(mActivity).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            downloadApk.downloadApk(info.getAppUrl());
                        } else {
                            showToast(R.string.str_need_sdcard_permission);
//                            Utils.toastShort(mActivity, mActivity.getString(R.string.str_need_sdcard_permission));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Utils.onError(throwable);
                    }
                });
            }

            @Override
            public void onFail() {
            }
        }).showDialog(mActivity.getSupportFragmentManager());

    }

    @Override
    public void onDownError() {
//        Utils.toastShort(mActivity, mActivity.getString(R.string.str_update_apk_fail));
        showToast(R.string.str_update_apk_fail);
    }
}
