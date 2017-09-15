package io.dcloud.H58E83894.http.callback;


import io.reactivex.disposables.Disposable;

public interface RequestCallback<T> {

    /**
     * 请求之前调用
     */
    void beforeRequest(Disposable d);

    /**
     * 请求错误调用
     *
     * @param msg 错误信息
     */
    void requestFail(String msg);

    /**
     * 请求完成调用
     */
    void requestComplete();

    /**
     * 请求成功调用
     *
     * @param t 数据
     */
    void requestSuccess(T t);

}
