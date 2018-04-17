package io.dcloud.H58E83894.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.http.callback.RequestCallback;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.common.SimpleLoginTipDialog;
import io.dcloud.H58E83894.ui.helper.FeedBackHelper;
import io.dcloud.H58E83894.ui.user.modify.ModifyNickActivity;
import io.dcloud.H58E83894.utils.ActivityCollector;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.FileUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.Response;
import zlc.season.rxdownload2.RxDownload;

public class BaseActivity extends FragmentActivity {
    protected Context mContext;
    protected String TAG = BaseActivity.this.getClass().getSimpleName();//获取该activity名称
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    protected RxPermissions mRxPermissions;
    private Map param = new HashMap();
    private String msg = "";
    private String nickName;

    //将需要被 CompositeDisposable 管理的 observer 加入到管理集合中
    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    //拨打电话
    protected void callPhone(final String phoneNumber) {
        mRxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Utils.callPhone(BaseActivity.this, phoneNumber);
                } else {
                    toastShort(R.string.str_call_phone_no_permisson);
                }
            }
        });
    }


    //上到下，下到上动画
    protected void topDownInAnim(View topView, View bottomView) {
        Animation topIn = AnimationUtils.loadAnimation(mContext, R.anim.practice_up_in);
        topView.startAnimation(topIn);
        Animation downIn = AnimationUtils.loadAnimation(mContext, R.anim.practice_down_in);
        bottomView.startAnimation(downIn);
    }

    //检查app是否登录
    protected void loginTip(Class<?> target) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            new SimpleLoginTipDialog().showDialog(getSupportFragmentManager());
            return;
        }
        forword(target);
    }

    protected void setDownloadDefalutPath(final RxDownload mRxDownload) {
        //检查写入权限
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("no permission");
                        }
                    }
                })
                .compose(new ObservableTransformer<Boolean, Boolean>() {
                    @Override
                    public ObservableSource<Boolean> apply(Observable<Boolean> upstream) {
                        return upstream.flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                            @Override
                            public ObservableSource<Boolean> apply(@NonNull Boolean aBoolean) throws Exception {
                                return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                        mRxDownload.defaultSavePath(FileUtil.getDownloadPath(mContext));
                                        e.onNext(true);
                                        e.onComplete();
                                    }
                                });
                            }
                        });
                    }
                }).subscribe();
    }

    /**
     * 设置recycle完全显示
     */
    protected void initRecycler(RecyclerView recycle, RecyclerView.LayoutManager manager) {
        manager.setAutoMeasureEnabled(true);//recycle是否开始主动测量
        recycle.setLayoutManager(manager);
        recycle.setHasFixedSize(true);//EtHasFixedSize 的作用就是确保尺寸是通过用户输入从而确保RecyclerView的尺寸是一个常数
        recycle.setNestedScrollingEnabled(false);
    }

    /**
     * 自动登录
     */
    public void login(final RequestCallback requestCallback) {
        String account = SharedPref.getAccount(mContext);
        String password = SharedPref.getPassword(mContext);
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            requestCallback.requestFail(C.ACCOUNT_PASSWORD_ISEMPTY);
            return;
        }
        final Map param = new HashMap();
        HttpUtil.login(account, password)
                .flatMap(new Function<UserInfo, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull UserInfo info) throws Exception {
                        if (getHttpResSuc(info.getCode())) {
                            //登录成功，重置session
                            param.put("uid", info.getUid());
                            param.put("username", info.getUsername());
                            param.put("password", info.getPassword());
                            param.put("email", info.getEmail());
                            param.put("phone", info.getPhone());
                            param.put("nickname", info.getNickname());
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
               /* .flatMap(new Function<Response<Void>, ObservableSource<ResultBean<UserData>>>() {
                    @Override
                    public ObservableSource<ResultBean<UserData>> apply(@NonNull Response<Void> response) throws Exception {
                        if (response.isSuccessful()) {
                            return HttpUtil.getUserDetailInfo();
                        } else {
                            throw new IllegalArgumentException("smartapply reset session fail");
                        }
                    }
                })*/
                .compose(new SchedulerTransformer<Response<Void>>())
                .subscribe(new ResultObserver<>(new RequestCallback<Response<Void>>() {
                    @Override
                    public void beforeRequest(Disposable d) {
                        addToCompositeDis(d);
                        if (requestCallback != null)
                            requestCallback.beforeRequest(d);
                    }

                    @Override
                    public void requestFail(String msg) {
                        if (requestCallback != null)
                            requestCallback.requestFail(msg);
                    }

                    @Override
                    public void requestComplete() {
                        if (requestCallback != null)
                            requestCallback.requestComplete();
                    }

                    @Override
                    public void requestSuccess(Response<Void> bean) {
//                        UserData data = bean.getData();
//                        String userJson = JsonUtil.toJson(data);
//                        SharedPref.saveLoginInfo(mContext, userJson);
//                        GlobalUser.getInstance().resetUserDataBySharedPref(userJson);
//                        GlobalUser.getInstance().setUserData(data);
                        if (!bean.isSuccessful()) {
                            requestCallback.requestFail("smart reset session fail");
                            return;
                        }
                        String nickname = (String) param.get("nickname");
                        if (TextUtils.isEmpty(nickname)) {
                            //去设置昵称页面
                            forword(ModifyNickActivity.class);
                        } else {
                            //登录成功
                            RxBus.get().post(C.LOGIN_INFO, true);
                        }
                        if (requestCallback != null)
                            requestCallback.requestSuccess("");
                    }
                }));
    }

    protected void showFeedBackDialog(String id) {
        FeedBackHelper.showDialog(id, getSupportFragmentManager());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        FeedBackHelper.destory();
//        if (needDispose())
//            ReSetSessionProxy.getInstance().onDestroy();
        WaitDialog.destroyDialog(this);
    }

    protected String throwMsg(Throwable throwable) {
        return Utils.onError(throwable);
    }

    protected void errorTip(Throwable throwable) {
        String s = Utils.onError(throwable);
        if (!TextUtils.isEmpty(s))
            toastShort(s);
    }

//    protected boolean needDispose() {
//        return false;
//    }

    public void showLoadDialog() {
        WaitDialog.getInstance(mContext).showWaitDialog();
    }

    public void dismissLoadDialog() {
        if (WaitDialog.getInstance(mContext) != null && WaitDialog.getInstance(mContext).isShowing()){
            WaitDialog.getInstance(mContext).dismissWaitDialog();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        ActivityCollector.addActivity(this);//集合activity，退出app操作
        getArgs();
        initData();
        initView();
        asyncUiInfo();
    }

    protected void initView() {

    }

    public boolean needAgainLogin(ResultBean bean) {
        if (bean.getMessage().contains(getString(R.string.str_no_login_one)) || bean.getMessage().contains(getString(R.string.str_no_login_two))) {
            return true;
        }
        return false;
    }


    protected void log(String msg) {
        Utils.logh(TAG, msg);
    }

    protected String getEditText(EditText et) {
        return Utils.getEditTextString(et);
    }

    protected boolean getHttpResSuc(int code) {
        if (Utils.getHttpMsgSu(code)) {
            return true;
        }
        return false;
    }

    /**
     * 获取传递的数据
     */
    protected void getArgs() {

    }

    protected void initData() {

    }

    protected void toastShort(int id) {
        Utils.toastShort(mContext, id);
    }

    protected void toastShort(String msg) {
        Utils.toastShort(mContext, msg);
    }

    /**
     * 获取基本数据后刷新
     */
    protected void asyncUiInfo() {

    }

    //跳转activity
    protected void forword(Class<?> c) {
        startActivity(new Intent(this, c));
    }

//    protected boolean isAccountInvalid(int requestCode) {
//        if (GlobalUser.getInstance().isAccountDataInvalid()) {
//            //无效，去登录
//            Intent intent = new Intent(mContext, LoginAactivity.class);
//            startActivityForResult(intent, requestCode);
//            return true;
//        }
//        return false;
//    }

    /**
     * 退出之前，如果需要额外处理，调用此方法
     *
     * @return {@link #onKeyDown(int, KeyEvent) onKeyDown}后续执行
     * true：	直接返回，停留在当前页面；
     * false：	继续执行退出后续操作。
     */
    protected boolean preBackExitPage() {
        return false;
    }

    /*
    * 左上角back按钮
    */
    public void leftBack(View view) {
        if (preBackExitPage()) {
            return;
        }
        finishWithAnim();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (preBackExitPage()) {
                return true;
            }
            finishWithAnim();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finishWithAnim() {
        switch (getAnimType()) {
            case ANIM_TYPE_RIGHT_IN:
                finishWithAnimRightOut();
                break;
            case ANIM_TYPE_UP_IN:
                finishWithAnimDownOut();
                break;
            case ANIM_TYPE_DOWN_IN:
                finishWithAnimUpOut();
                break;
            case ANIM_TYPE_SCALE_CENTER:
                finishWithAnimScaleCenter();
                break;
            default:
                finish();
                break;
        }
    }


    private void finishWithAnimRightOut() {
        finish();
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
    }

    private void finishWithAnimUpOut() {
        finish();
        overridePendingTransition(0, R.anim.ac_slide_up_out);
    }

    private void finishWithAnimDownOut() {
        finish();
        overridePendingTransition(0, R.anim.ac_slide_down_out);
    }

    private void finishWithAnimScaleCenter() {
        finish();
        overridePendingTransition(0, R.anim.ac_scale_shrink_center);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getAnimType()) {
            case ANIM_TYPE_RIGHT_IN:
                overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
                break;
            case ANIM_TYPE_UP_IN:
                overridePendingTransition(R.anim.ac_slide_up_in, 0);
                break;
            case ANIM_TYPE_DOWN_IN:
                overridePendingTransition(R.anim.ac_slide_down_in, 0);
                break;
            case ANIM_TYPE_SCALE_CENTER:
                overridePendingTransition(R.anim.ac_scale_magnify_center, 0);
                break;
            default:
                break;
        }
        mContext = this;
        mRxPermissions = new RxPermissions(this);
    }

    public enum AnimType {
        ANIM_TYPE_DOWN_IN,
        ANIM_TYPE_RIGHT_IN, // 右侧滑动进入
        ANIM_TYPE_UP_IN, //
        ANIM_TYPE_SCALE_CENTER // 中心缩放显示/隐藏
    }

    public AnimType getAnimType() {
        return AnimType.ANIM_TYPE_RIGHT_IN;
    }


    /**
     * 未登录弹框提示用户
     *
     * @return true 未登录
     */
    protected boolean needLogin() {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            new SimpleLoginTipDialog().showDialog(getSupportFragmentManager());
            return true;
        }
        return false;
    }



}
