package com.boatchina.imerit.data.device;

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
public class DeviceRepo {
    private DeviceApi deviceApi;
    @Inject
    DeviceRepo(DeviceApi deviceApi) {
        this.deviceApi = deviceApi;
    }

    public Observable<BlankEntity> deviceSetInfo(String token, String imei, String name, String phone) {
        return deviceApi.device_set_info(token, imei, name,phone)
                .map(new HttpResultFunc<>());
    }

    public Observable<DeviceEntity> deviceGetInfo(String token, String imei) {
        return deviceApi.device_get_info(token, imei)
                .map(new HttpResultFunc<>());
    }
    public Observable<BlankEntity> Bind(String token1, String token2,String imei,String nick) {
        return deviceApi.Bind(token1, token2, imei, nick)
                .map(new HttpResultFunc<>());
    }
    public Observable<BlankEntity> unBind(String token, String imei) {
        return deviceApi.unBind(token, imei)
                .map(new HttpResultFunc<>());
    }
    public Observable<List<DeviceEntity>> getBonds(String token) {
        return deviceApi.getBonds(token)
                .map(new HttpResultFunc<>());
    }
    public Observable<BlankEntity> Config(String token, String imei, int interval) {
        return deviceApi.config(token, imei, interval)
                .map(new HttpResultFunc<>());
    }


    public Observable<BlankEntity> bindreq(String token1, String token2, String imei, String msg) {
        return deviceApi.bindreq(token1, token2, imei, msg)
                .map(new HttpResultFunc<>());
    }

    public Observable<BlankEntity> bindrsp(String token1, String token2, String imei, String user, String result) {
        return deviceApi.bindrsp(token1, token2, imei, user, result)
                .map(new HttpResultFunc<>());
    }

    public Observable<BlankEntity> dealevents(String token, String events) {
        return deviceApi.dealevents(token, events)
                .map(new HttpResultFunc<>());
    }

    public Observable<BlankEntity> passivecall(String token, String imei, String number) {
        return deviceApi.passivecall(token, imei, number)
                .map(new HttpResultFunc<>());
    }
}
