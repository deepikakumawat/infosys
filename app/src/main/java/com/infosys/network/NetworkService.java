package com.infosys.network;

import com.infosys.model.InfoData;

import retrofit2.http.GET;
import rx.Observable;

public interface NetworkService {

    @GET("s/2iodh4vg0eortkl/facts.json")
    Observable<InfoData> getList();

}
