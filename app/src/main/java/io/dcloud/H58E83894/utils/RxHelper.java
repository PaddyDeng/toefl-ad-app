package io.dcloud.H58E83894.utils;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by fire on 2017/9/7  17:08.
 */

public class RxHelper {

    private static void log(String msg) {
        Utils.logh(RxHelper.class.getSimpleName(), msg);
    }

    static {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                if (throwable instanceof InterruptedException) {
                    log("Thread interrupted");
                } else if (throwable instanceof InterruptedIOException) {
                    log("Io interrupted");
                } else if (throwable instanceof SocketException) {
                    log("Socket error");
                } else {
                    throwable.printStackTrace();
                }
            }
        });
    }

    public static Observable<Integer> countDown(final int time) {
        return Observable
                .interval(1, TimeUnit.SECONDS)
                .take(time + 1)
                .flatMap(new Function<Long, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull Long aLong) throws Exception {
                        return Observable.just(time - aLong.intValue());
                    }
                })
                .compose(new SchedulerTransformer<Integer>());
    }

    public static Observable<Integer> delay(final int time) {
        return Observable
                .just(time)
                .delay(time, TimeUnit.SECONDS)
                .compose(new SchedulerTransformer<Integer>());
    }
}
