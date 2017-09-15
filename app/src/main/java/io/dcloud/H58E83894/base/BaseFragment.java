package io.dcloud.H58E83894.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.leakcanary.RefWatcher;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.common.SimpleLoginTipDialog;
import io.dcloud.H58E83894.ui.helper.FeedBackHelper;
import io.dcloud.H58E83894.ui.user.UserProxyActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import retrofit2.Response;


public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    private String TAG = BaseFragment.this.getClass().getSimpleName();
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    protected void log(String msg) {
        Utils.logh(TAG, msg);
    }

    protected String getEditText(EditText et) {
        return Utils.getEditTextString(et);
    }

    protected void showLoadDialog() {
        WaitDialog.getInstance(getActivity()).showWaitDialog();
    }

    protected void dismissLoadDialog() {
        WaitDialog.getInstance(getActivity()).dismissWaitDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        FeedBackHelper.destory();
//        if (needDispose())
//            ReSetSessionProxy.getInstance().onDestroy();

        WaitDialog.destroyDialog(getActivity());

        RefWatcher refWatcher = ToeflApplication.getRefWatcher(getActivity());
        if (null != refWatcher) refWatcher.watch(this);
    }

//    protected boolean needDispose() {
//        return false;
//    }

    protected String errorTip(Throwable throwable) {
        return Utils.onError(throwable);
    }

    /**
     * 设置recycle完全显示
     */
    protected void initRecycler(RecyclerView recycle, RecyclerView.LayoutManager manager) {
        manager.setAutoMeasureEnabled(true);
        recycle.setLayoutManager(manager);
        recycle.setHasFixedSize(true);
        recycle.setNestedScrollingEnabled(false);
    }

    protected void initTabAdapter(PagerAdapter adapter, ViewPager mViewPager, TabLayout mTabLayout) {
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    /**
     * 1 返回数据成功且为1
     * 2 自动登录成功
     * 3 你未登录，去登录
     */
    protected Observable<Integer> loginExpired(ResultBean bean) {
        if (getHttpResSuc(bean.getCode())) {
            return Observable.just(C.RESPONSE_SUCCESS);
        }
        if (bean.getMessage().contains(getString(R.string.str_no_login_one)) || bean.getMessage().contains(getString(R.string.str_no_login_two))) {
            return login();
        }
        toastShort(bean.getMessage());
        return Observable.just(C.RESPONSE_FAIL);
    }

    private Map param = new HashMap();
    private String msg = "";
    private String nickName;

    protected Observable<Integer> login() {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            toastShort(getString(R.string.str_no_login_tip));
            return Observable.just(C.NO_LOGIN);
        }
        return HttpUtil.login(SharedPref.getAccount(getActivity()), SharedPref.getPassword(getActivity()))
                .flatMap(new Function<UserInfo, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull UserInfo info) throws Exception {
                        msg = info.getMessage();
                        if (getHttpResSuc(info.getCode())) {
                            //登录成功，重置session
                            nickName = info.getNickname();
                            param.put("uid", info.getUid());
                            param.put("username", info.getUsername());
                            param.put("password", info.getPassword());
                            param.put("email", info.getEmail());
                            param.put("phone", info.getPhone());
                            param.put("nickname", nickName);
                            return HttpUtil.toefl(param);
                        } else {
                            throw new IllegalArgumentException(info.getMessage());
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.gossip(param);
                        } else {
                            throw new IllegalArgumentException("toefl reset session fail");
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.gmatl(param);
                        } else {
                            throw new IllegalArgumentException("gossip reset session fail");
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.smartapply(param);
                        } else {
                            throw new IllegalArgumentException("gmatl reset session fail");
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<ResultBean<UserData>>>() {
                    @Override
                    public ObservableSource<ResultBean<UserData>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.getUserDetailInfo();
                        } else {
                            throw new IllegalArgumentException("smartapply reset session fail");
                        }
                    }
                })
                .flatMap(new Function<ResultBean<UserData>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull ResultBean<UserData> bean) throws Exception {
                        if (bean == null) {
                            return Observable.just(C.AUTO_LOGIN_FAIL);
                        }
                        return Observable.just(C.AUTO_LOGIN_SUCCESS);
                    }
                });
//                .compose(new SchedulerTransformer<ResultBean<UserData>>())
//                .subscribe(new ResultObserver<>(new RequestCallback<ResultBean<UserData>>() {
//                    @Override
//                    public void beforeRequest(Disposable d) {
//                        addToCompositeDis(d);
//                    }
//
//                    @Override
//                    public void requestFail(String msg) {
//                        toastShort(msg);
//                    }
//
//                    @Override
//                    public void requestComplete() {
//
//                    }
//
//                    @Override
//                    public void requestSuccess(ResultBean<UserData> bean) {
//                        toastShort(msg);
//                        UserData data = bean.getData();
//                        String userJson = JsonUtil.toJson(data);
//                        SharedPref.saveLoginInfo(getActivity(), userJson);
//                        GlobalUser.getInstance().resetUserDataBySharedPref(userJson);
//                        GlobalUser.getInstance().setUserData(data);
//                        if (TextUtils.isEmpty(data.getNickname())) {
//                            //去设置昵称页面
//                            forword(ModifyNickActivity.class);
//                        } else {
//                            //登录成功，关闭页面
//                            RxBus.get().post(C.LOGIN_INFO, true);
//                        }
//                    }
//                }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final boolean rootNull = mRootView == null;
        if (rootNull) {
            mRootView = onCreateViewInit(inflater, container,
                    savedInstanceState);
        }
        ButterKnife.bind(this, mRootView);
        getArgs();
        if (rootNull) {
            initWhenRootViewNull(savedInstanceState);
        }
        return mRootView;
    }

    /**
     * 获取传递的参数
     */
    protected void getArgs() {
    }

    /**
     * 在这里初始化数据
     */
    protected void initWhenRootViewNull(Bundle savedInstanceState) {
    }

    /**
     * true 字符串至少有一个为空
     * false 字符串不为空
     */
    protected boolean isEmpty(String... str) {
        return Utils.isEmpty(str);
    }

    /**
     * 刷新界面
     */
    protected void refreshUi() {

    }

    /**
     * 重写此函数来获取view
     */
    protected abstract View onCreateViewInit(LayoutInflater inflater,
                                             ViewGroup container, Bundle savedInstanceState);


    protected void toastShort(int id) {
        Utils.toastShort(getActivity(), id);
    }

    protected void toastShort(String msg) {
        Utils.toastShort(getActivity(), msg);
    }

    protected void forword(Class<?> c) {
        startActivity(new Intent(getActivity(), c));
    }

    protected boolean isAccountInvalid(int requestCode) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            //无效，去登录
            Intent intent = new Intent(getActivity(), UserProxyActivity.class);
            startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    protected boolean getHttpResSuc(int code) {
        if (Utils.getHttpMsgSu(code)) {
            return true;
        }
        return false;
    }

    protected void loginTip(Class<?> target) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            new SimpleLoginTipDialog().showDialog(getChildFragmentManager());
            return;
        }
        forword(target);
    }

    protected boolean needLogin() {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            new SimpleLoginTipDialog().showDialog(getChildFragmentManager());
            return true;
        }
        return false;
    }
}
