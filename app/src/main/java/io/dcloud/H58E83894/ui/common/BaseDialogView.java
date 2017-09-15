package io.dcloud.H58E83894.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseDialogView extends BaseDialogFragment {

    protected View mRootView;

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final boolean isNull = mRootView == null;
        if (mRootView == null) {
            mRootView = LayoutInflater.from(getActivity()).inflate(getRootViewId(), container, false);
            initRootView(savedInstanceState);
        }
        ButterKnife.bind(this, mRootView);
        getArgs();
        if (isNull) initViewData();
        return mRootView;
    }

    protected void initRootView(Bundle savedInstanceState) {
    }

    protected abstract int getRootViewId();

    protected void toastShort(String msg) {
        Utils.toastShort(getActivity(), msg);
    }

    protected void toastShort(@StringRes int resId) {
        Utils.toastShort(getActivity(), resId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mRootView) {
            ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
            if (null != viewGroup) {
                viewGroup.removeView(mRootView);
            }
        }
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        WaitDialog.destroyDialog(getActivity());

        RefWatcher refWatcher = ToeflApplication.getRefWatcher(getActivity());
        if (null != refWatcher) refWatcher.watch(this);
    }


    protected void showLoadDialog() {
        WaitDialog.getInstance(getActivity()).showWaitDialog();
    }

    protected void dismissLoadDialog() {
        WaitDialog.getInstance(getActivity()).dismissWaitDialog();
    }


    protected void getArgs() {
    }

    /**
     * 首次执行调用。在获取了控件的id后
     */
    protected void initViewData() {
    }
}
