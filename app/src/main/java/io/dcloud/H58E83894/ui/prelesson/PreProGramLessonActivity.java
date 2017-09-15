package io.dcloud.H58E83894.ui.prelesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.RegexValidateUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class PreProGramLessonActivity extends BaseActivity {

    public static void startPre(Context c, String teachId, String lessonName) {
        Intent intent = new Intent(c, PreProGramLessonActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, teachId);
        intent.putExtra(Intent.EXTRA_TITLE, lessonName);
        c.startActivity(intent);
    }

    @BindView(R.id.prepro_et_phone)
    EditText proPhone;
    @BindView(R.id.prepro_et_name)
    EditText proName;

    private String teachId;
    private String lessonName;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        lessonName = intent.getStringExtra(Intent.EXTRA_TITLE);
        teachId = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pre_pro_gram_lesson);
    }

    @OnClick({R.id.place_one_view, R.id.place_two_view, R.id.place_thr_view, R.id.place_close,
            R.id.prepro_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.place_close:
            case R.id.place_one_view:
            case R.id.place_two_view:
            case R.id.place_thr_view:
                finishWithAnim();
                break;
            case R.id.prepro_commit:
                commit();
                break;
            default:
                break;
        }
    }

    private void commit() {
        String name = getEditText(proName);
        String phone = getEditText(proPhone);
        if (TextUtils.isEmpty(name)) {
            toastShort(R.string.str_enter_you_name_tip);
            return;
        }
        if (!RegexValidateUtil.checkPhoneNumber(phone)) {
            toastShort(R.string.str_phone_error_tip);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            toastShort(R.string.str_enter_you_phone_tip);
            return;
        }
        String[] arr = new String[]{teachId.trim(),phone.trim(),lessonName.trim(),"android toefl app"};
        addToCompositeDis(HttpUtil.addContent(name, arr).subscribe(new Consumer<ResultBean>() {
            @Override
            public void accept(@NonNull ResultBean bean) throws Exception {
                if (getHttpResSuc(bean.getCode())) {
                    finishWithAnim();
                } else {
                    toastShort(bean.getMessage());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        }));
    }


    @Override
    public AnimType getAnimType() {
        return AnimType.ANIM_TYPE_UP_IN;
    }
}
