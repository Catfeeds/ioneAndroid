package com.boatchina.imerit.data.location;

import com.boatchina.imerit.data.net.HttpResultFunc;
import com.boatchina.imerit.data.PerActivity;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Administrator on 2017/2/22.
 */
@PerActivity
public class LocationRepo {
    private LocationApi locationApi;
    @Inject
    LocationRepo(LocationApi locationApi) {
        this.locationApi = locationApi;
    }



    public Observable<LocationEntity> location(String token, String imei, String coordtype) {
        return locationApi.location(token, imei, coordtype)
                .map(new HttpResultFunc<>());
    }

    public Observable<List<LocationEntity>> history(String token, String imei, long begin, long end, String coordtype) {
        return locationApi.history(token, imei, begin, end,coordtype)
                .map(new HttpResultFunc<>());
    }
}
