package com.boatchina.imerit.data.fence;

import com.boatchina.imerit.data.net.HttpResultFunc;
import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.entity.BlankEntity;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Administrator on 2017/2/22.
 */
@PerActivity
public class FenceRepo {
    private FenceApi fenceApi;
    @Inject
    FenceRepo(FenceApi fenceApi) {
        this.fenceApi = fenceApi;
    }

    public Observable<FenceEntity> addFine(String token, String imei, String name, String type, String lng1, String lat1, String lng2, String lat2, String radius) {
        return fenceApi.addfine(token, imei, name, type, lng1, lat1, lng2, lat2,radius)
                .map(new HttpResultFunc<>());
    }

    public Observable<FenceEntity> updatefine(int fine, String token, String imei, String name, String type, String lng1, String lat1, String lng2, String lat2,String radius) {
        return fenceApi.updatefine(fine,token, imei, name, type, lng1, lat1, lng2, lat2,radius)
                .map(new HttpResultFunc<>());
    }


    public Observable<BlankEntity> delFine(String token, String id) {
        return fenceApi.delfine(token,id)
                .map(new HttpResultFunc<>());
    }

    public Observable<List<FenceEntity>> getFineList(String token, String imei) {
        return fenceApi.getfinelist(token, imei)
                .map(new HttpResultFunc<>());
    }
}
