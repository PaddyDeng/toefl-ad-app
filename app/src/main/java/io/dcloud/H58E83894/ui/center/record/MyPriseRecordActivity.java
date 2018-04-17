package io.dcloud.H58E83894.ui.center.record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.user.UserInfo;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.center.glossary.GlossaryActivity;
import io.dcloud.H58E83894.ui.center.lesson.MyLessonActivity;
import io.dcloud.H58E83894.ui.center.message.MsgActivity;
import io.dcloud.H58E83894.ui.center.record.listen.ListenRecordActivity;
import io.dcloud.H58E83894.ui.center.record.read.ReadRecordActivity;
import io.dcloud.H58E83894.ui.center.record.speak.SpeakRecordActivity;
import io.dcloud.H58E83894.ui.center.record.write.WriteRecordActivity;
import io.dcloud.H58E83894.ui.center.setting.SettingActivity;
import io.dcloud.H58E83894.ui.user.LoginFragment;
import io.dcloud.H58E83894.ui.user.OnUserInfoListener;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RegexValidateUtil;
import io.reactivex.Observable;

public class MyPriseRecordActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prise_record);
    }


    @OnClick({R.id.wrong_record_container, R.id.center_buy_record_container, R.id.center_listen_record_container, R.id.center_make_record_container,
           })

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.wrong_record_container:
                loginTip(ReadRecordActivity.class);
                break;
            case R.id.center_buy_record_container:
                loginTip(WriteRecordActivity.class);
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
