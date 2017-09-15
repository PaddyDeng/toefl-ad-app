package io.dcloud.H58E83894.ui.make.bottom.sp.listener;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/31  09:56.
 */

public class SpeakPraiseListener implements View.OnClickListener {
    private String id;
    private Context mContext;

    public SpeakPraiseListener(String id, Context context) {
        this.id = id;
        mContext = context;
    }

    @Override
    public void onClick(final View v) {
        HttpUtil.speakPraise(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        WaitDialog.getInstance(mContext).showWaitDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        WaitDialog.getInstance(mContext).dismissWaitDialog();
                        ((TextView)v).setText(mContext.getString(R.string.str_speak_praise_num, String.valueOf(bean.getLiked())));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        WaitDialog.getInstance(mContext).dismissWaitDialog();
                        Utils.toastShort(mContext, Utils.onError(throwable));
                    }
                });
    }
}
