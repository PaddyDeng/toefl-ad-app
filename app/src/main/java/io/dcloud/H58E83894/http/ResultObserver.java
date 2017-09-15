package io.dcloud.H58E83894.http;

import io.dcloud.H58E83894.http.callback.RequestCallback;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by fire on 2017/7/13.
 */

public class ResultObserver<T> implements Observer<T> {

    private RequestCallback<T> mRequestCallback;

    public ResultObserver(RequestCallback<T> requestCallback) {
        if (null == requestCallback) {
            throw new NullPointerException("RequestCallBack is null");
        }
        mRequestCallback = requestCallback;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mRequestCallback.beforeRequest(d);
    }

    @Override
    public void onNext(T t) {
        mRequestCallback.requestSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mRequestCallback.requestFail(Utils.onError(e));
    }

    @Override
    public void onComplete() {
        mRequestCallback.requestComplete();
    }
}
