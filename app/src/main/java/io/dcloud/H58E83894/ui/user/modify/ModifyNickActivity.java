package io.dcloud.H58E83894.ui.user.modify;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.http.callback.RequestCallback;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.JsonUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.disposables.Disposable;

public class ModifyNickActivity extends BaseActivity implements RequestCallback<ResultBean> {

    @BindView(R.id.act_set_nick_name_tv)
    EditText setNickTv;
    @BindView(R.id.act_set_nick_confirm_btn)
    TextView confirmBtn;
    private boolean hasNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nick);
    }

    @Override
    protected void initView() {
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            String nickname = GlobalUser.getInstance().getUserData().getNickname();
            if (!TextUtils.isEmpty(nickname)) {
                hasNickName = true;
            }
        }
        confirmBtn.setClickable(false);
        setNickTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enable(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void enable(String s) {
        if (s.length() >= 2 && s.length() <= 8) {
            confirmBtn.setClickable(true);
            confirmBtn.setTextColor(getResources().getColor(R.color.color_white));
        } else {
            confirmBtn.setClickable(false);
            confirmBtn.setTextColor(getResources().getColor(R.color.color_gray));
        }
    }


    @OnClick({R.id.act_set_nick_confirm_btn, R.id.act_set_nick_cancel_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_set_nick_confirm_btn:
                setNickName();
                break;
            case R.id.act_set_nick_cancel_btn:
                if (!hasNickName) {
                    toastShort(R.string.str_set_nick_title);
                    return;
                }
                finishWithAnim();
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean preBackExitPage() {
        if (!hasNickName) {
            toastShort(R.string.str_set_nick_title);
            return true;
        }
        return super.preBackExitPage();
    }

    private boolean checkName(final EditText editText) {
        String name = Utils.getEditTextString(editText);
        if (TextUtils.isEmpty(name)) {
            toastShort(R.string.str_set_nick_name_tip);
            return false;
        } else if (name.equals(GlobalUser.getInstance().getUserData().getNickname())) {
            toastShort(R.string.str_set_modify_com);
            return false;
        }
        return true;
    }

    private void setNickName() {
        if (!checkName(setNickTv))
            return;
        final String nickName = getEditText(setNickTv);
        HttpUtil.modifyName(nickName).subscribe(new ResultObserver<ResultBean>(this));
    }

    @Override
    public void beforeRequest(Disposable d) {
        showLoadDialog();
        addToCompositeDis(d);
    }

    @Override
    public void requestFail(String msg) {
        dismissLoadDialog();
        toastShort(msg);
    }

    @Override
    public void requestComplete() {
        dismissLoadDialog();
    }

    @Override
    public void requestSuccess(ResultBean bean) {
        toastShort(bean.getMessage());
        if (getHttpResSuc(bean.getCode())) {
            saveUserInf(getEditText(setNickTv));
            RxBus.get().post(C.LOGIN_INFO, true);
            RxBus.get().post(C.MODIFY_INFO, C.MODIFY_NICKNAME);
            hasNickName = true;
            finishWithAnim();
        }
    }

    private void saveUserInf(String name) {
        if (!TextUtils.isEmpty(name)) {
            GlobalUser.getInstance().setNickName(name);
        }
        SharedPref.saveLoginInfo(mContext, JsonUtil.toJson(GlobalUser.getInstance().getUserData()));
    }
}
