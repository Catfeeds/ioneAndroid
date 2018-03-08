package com.boatchina.imerit.data.device;

import com.boatchina.imerit.data.entity.BaseEntity;
import com.boatchina.imerit.data.entity.BlankEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/22.
 */

public interface DeviceApi {

    @GET("tp/index.php/Device/device_set_info")
    Observable<BaseEntity<BlankEntity>> device_set_info(@Query("token") String token, @Query("imei") String imei, @Query("name") String name, @Query("phone") String phone);

    @GET("tp/index.php/device/device_get_info")
    Observable<BaseEntity<DeviceEntity>> device_get_info(@Query("token") String token, @Query("imei") String imei);

    @GET("tp/index.php/device/device_config")
    Observable<BaseEntity<BlankEntity>> config(@Query("token") String token, @Query("imei") String imei, @Query("interval") int interval);
    @GET("tp/index.php/Bind/bind")
    Observable<BaseEntity<BlankEntity>> Bind(@Query("token1") String token1, @Query("token2") String token2, @Query("imei") String imei,@Query("nick") String nick);
    @GET("tp/index.php/Bind/unbind")
    Observable<BaseEntity<BlankEntity>> unBind(@Query("token") String token,@Query("imei") String imei);
    @GET("tp/index.php/bind/GetBonds")
    Observable<BaseEntity<List<DeviceEntity>>> getBonds(@Query("token") String token);


    @POST("tp/index.php/bind/bindreq")
    Observable<BaseEntity<BlankEntity>> bindreq(@Query("token1") String token1, @Query("token2") String token2, @Query("imei") String imei, @Query("msg") String msg);
    @POST("tp/index.php/bind/bindRsp")
    Observable<BaseEntity<BlankEntity>> bindrsp(@Query("token1") String token1, @Query("token2") String token2, @Query("imei") String imei, @Query("user") String user, @Query("result") String result);
    @POST("tp/index.php/event/dealevents")
    Observable<BaseEntity<BlankEntity>> dealevents(@Query("token") String token, @Query("events") String events);
    @POST("tp/index.php/voice/passivecall")
    Observable<BaseEntity<BlankEntity>> passivecall(@Query("token") String token, @Query("imei") String imei, @Query("number") String number);

}
