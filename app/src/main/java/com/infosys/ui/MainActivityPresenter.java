package com.infosys.ui;

import com.infosys.model.InfoData;
import com.infosys.network.NetworkError;
import com.infosys.network.Service;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainActivityPresenter {
    private final Service service;
    private final MainActivityView view;
    private CompositeSubscription subscriptions;

    public MainActivityPresenter(Service service, MainActivityView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void getList() {
        view.showLoading();

        Subscription subscription = service.getList(new Service.GetListCallback() {
            @Override
            public void onSuccess( InfoData infoData) {
                view.hideLoading();
                view.getListSuccess(infoData);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.hideLoading();
                view.onFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}

