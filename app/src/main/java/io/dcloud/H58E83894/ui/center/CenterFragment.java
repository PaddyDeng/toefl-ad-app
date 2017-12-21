package io.dcloud.H58E83894.ui.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.center.glossary.GlossaryActivity;
import io.dcloud.H58E83894.ui.center.lesson.MyLessonActivity;
import io.dcloud.H58E83894.ui.center.message.MsgActivity;
import io.dcloud.H58E83894.ui.center.record.listen.ListenRecordActivity;
import io.dcloud.H58E83894.ui.center.record.read.ReadRecordActivity;
import io.dcloud.H58E83894.ui.center.record.speak.SpeakRecordActivity;
import io.dcloud.H58E83894.ui.center.record.write.WriteRecordActivity;
import io.dcloud.H58E83894.ui.center.setting.SettingActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.JsonUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.SharedPref;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/12.
 */

public class CenterFragment extends BaseFragment {
    private int i;
    private Observable<Boolean> loginInfo;
    @BindView(R.id.center_go_login)
    TextView nameTv;
    @BindView(R.id.center_header_iv)
    ImageView headImg;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_center_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setUi();
        getUserDetailInfo();
        loginInfo = RxBus.get().register(C.LOGIN_INFO, Boolean.class);
        loginInfo.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                //aBoolean true 登录成功
                setUi();
            }
        });
    }

    private void getUserDetailInfo() {
        addToCompositeDis(HttpUtil.getUserDetailInfo()
                .compose(new SchedulerTransformer<ResultBean<UserData>>())
                .subscribe(new Consumer<ResultBean<UserData>>() {
                    @Override
                    public void accept(@NonNull ResultBean<UserData> bean) throws Exception {
                        UserData data = bean.getData();
                        if (data != null) {
                            String userJson = JsonUtil.toJson(data);
                            SharedPref.saveLoginInfo(getActivity(), userJson);
                            GlobalUser.getInstance().resetUserDataBySharedPref(userJson);
                            GlobalUser.getInstance().setUserData(data);
                        }
                        setUi();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void setUi() {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            nameTv.setText(getString(R.string.str_no_login));
            headImg.setImageResource(R.drawable.icon_default);
        } else {
            UserData data = GlobalUser.getInstance().getUserData();
            nameTv.setText(data.getNickname());
            if (!TextUtils.isEmpty(data.getImage())) {
                GlideUtil.load(RetrofitProvider.TOEFLURL + data.getImage(), headImg);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != loginInfo)
            RxBus.get().unregister(C.LOGIN_INFO, loginInfo);
    }

    @OnClick({R.id.center_go_login, R.id.center_person_info_container, R.id.more_setting_container,
            R.id.wrong_record_container, R.id.center_buy_record_container, R.id.center_msg_container,
            R.id.center_glossary_container, R.id.center_listen_record_container, R.id.center_make_record_container,
            R.id.set_collection_container})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.center_go_login:
                if (!isAccountInvalid(C.REQUEST_LOGIN)) {
                    //登录有效。
                }
//                forword(UserProxyActivity.class);
                break;
            case R.id.set_collection_container:
                loginTip(MyLessonActivity.class);
                break;
            case R.id.center_person_info_container:
                break;
            case R.id.more_setting_container:
                forword(SettingActivity.class);
                break;
            case R.id.wrong_record_container:
                loginTip(ReadRecordActivity.class);
                break;
            case R.id.center_buy_record_container:
                loginTip(WriteRecordActivity.class);
                break;
            case R.id.center_msg_container:
                loginTip(MsgActivity.class);
                break;
            case R.id.center_glossary_container:
                loginTip(GlossaryActivity.class);
                break;
            case R.id.center_listen_record_container:
                loginTip(ListenRecordActivity.class);
                break;
            case R.id.center_make_record_container:
                loginTip(SpeakRecordActivity.class);
                break;
            default:
                break;
        }
    }

}
