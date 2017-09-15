package io.dcloud.H58E83894.utils;

import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;
import zlc.season.rxdownload2.entity.DownloadStatus;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by fire on 2017/7/20  17:02.
 */

public class DownloadUtil {
    public static void download(final String url, final RxDownload rxDownload, RxPermissions rxPermissions, final RequestImp<DownloadStatus> requestImp) {
        rxPermissions.request(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("no permission");
                        }
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<DownloadRecord>>() {
                    @Override
                    public ObservableSource<DownloadRecord> apply(@NonNull Boolean aBoolean) throws Exception {
                        rxDownload.defaultSavePath(FileUtil.getDownloadPath(ToeflApplication.getInstance()));
                        return rxDownload.getDownloadRecord(url);
                    }
                })
                .compose(new ObservableTransformer<DownloadRecord, Boolean>() {
                    @Override
                    public ObservableSource<Boolean> apply(Observable<DownloadRecord> upstream) {
                        return upstream.flatMap(new Function<DownloadRecord, ObservableSource<Boolean>>() {
                            @Override
                            public ObservableSource<Boolean> apply(@NonNull final DownloadRecord record) throws Exception {
                                return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                        e.onNext(true);
                                        e.onComplete();
                                    }
                                });
                            }
                        });
                    }
                })
                .observeOn(Schedulers.io())
                .compose(rxDownload.<Boolean>transform(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadStatus>() {
                    @Override
                    public void accept(@NonNull DownloadStatus status) throws Exception {
                        requestImp.requestSuccess(status);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        requestImp.requestFail(Utils.onError(throwable));
                    }
                });
    }

    public static void downloadService(final String url, final RxDownload rxDownload, RxPermissions rxPermissions) {
        rxPermissions.request(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("no permission");
                        }
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<DownloadRecord>>() {
                    @Override
                    public ObservableSource<DownloadRecord> apply(@NonNull Boolean aBoolean) throws Exception {
                        rxDownload.defaultSavePath(FileUtil.getDownloadPath(ToeflApplication.getInstance()));
                        return rxDownload.getDownloadRecord(url);
                    }
                })
                .compose(new ObservableTransformer<DownloadRecord, Boolean>() {
                    @Override
                    public ObservableSource<Boolean> apply(Observable<DownloadRecord> upstream) {
                        return upstream.flatMap(new Function<DownloadRecord, ObservableSource<Boolean>>() {
                            @Override
                            public ObservableSource<Boolean> apply(@NonNull final DownloadRecord record) throws Exception {
                                return Observable.create(new ObservableOnSubscribe<Boolean>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
//                                        if (record.getFlag() != DownloadFlag.COMPLETED) {
                                        rxDownload.maxThread(1);
                                        e.onNext(true);
                                        e.onComplete();
//                                        } else {
//                                            throw new RuntimeException("wait...");
//                                        }
                                    }
                                });
                            }
                        });
                    }
                })
                .observeOn(Schedulers.io())
                .compose(rxDownload.<Boolean>transformService(url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
