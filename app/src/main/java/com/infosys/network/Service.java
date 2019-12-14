package com.infosys.network;

import com.infosys.model.InfoData;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Service {
    private final NetworkService networkService;

    public Service(NetworkService networkService) {
        this.networkService = networkService;
    }

    public Subscription getList(final GetListCallback callback) {

        return networkService.getList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends InfoData>>() {
                    @Override
                    public Observable<? extends InfoData> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<InfoData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(InfoData infoData) {
                        callback.onSuccess(infoData);

                    }
                });
    }

    public interface GetListCallback{
        void onSuccess(InfoData infoData);

        void onError(NetworkError networkError);
    }
}
