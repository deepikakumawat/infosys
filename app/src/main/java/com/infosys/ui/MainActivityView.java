package com.infosys.ui;

import com.infosys.model.InfoData;

public interface MainActivityView {
    void showLoading();

    void hideLoading();

    void onFailure(String appErrorMessage);

    void getListSuccess(InfoData infoData);

}
