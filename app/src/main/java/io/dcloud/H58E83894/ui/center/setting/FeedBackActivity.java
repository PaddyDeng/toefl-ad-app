package io.dcloud.H58E83894.ui.center.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.feedBack_et)
    EditText feedContent;
    @BindView(R.id.feedBack_phone)
    EditText feedPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
    }

    @OnClick({R.id.feed_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feed_back:
                commitFeedBack();
                break;
            default:
                break;
        }
    }

    private void commitFeedBack() {
        String content = getEditText(feedContent);
        String phone = getEditText(feedPhone);
        if (TextUtils.isEmpty(content)) {
            return;
        }
        addToCompositeDis(HttpUtil.commitFeedBack(phone, content)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        toastShort(bean.getMessage());
                        if (getHttpResSuc(bean.getCode())) {
                            finishWithAnim();
                        }
                        dismissLoadDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }
}
