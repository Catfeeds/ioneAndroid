package com.boatchina.imerit.data.fence;

import com.boatchina.imerit.data.entity.BaseEntity;
import com.boatchina.imerit.data.entity.BlankEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/22.
 */

public interface FenceApi {
    @GET("tp/index.php/fence/fence_add")
    Observable<BaseEntity<FenceEntity>> addfine(@Query("token") String token, @Query("imei") String imei, @Query("name") String name, @Query("type") String type, @Query("lng1") String lng1, @Query("lat1") String lat1, @Query("lng2") String lng2, @Query("lat2") String lat2, @Query("radius") String radius);
    @GET("tp/index.php/fence/fence_update")
    Observable<BaseEntity<FenceEntity>> updatefine(@Query("id") int id, @Query("token") String token, @Query("imei") String imei, @Query("name") String name, @Query("type") String type, @Query("lng1") String lng1, @Query("lat1") String lat1, @Query("lng2") String lng2, @Query("lat2") String lat2,@Query("radius") String radius);

    @GET("tp/index.php/fence/fence_del")
    Observable<BaseEntity<BlankEntity>> delfine(@Query("token") String token, @Query("id") String id);

    @GET("tp/index.php/fence/fence_get_list")
    Observable<BaseEntity<List<FenceEntity>>> getfinelist(@Query("token") String token, @Query("imei") String imei);}
