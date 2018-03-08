package com.boatchina.imerit.data.user;

import com.boatchina.imerit.data.entity.BaseEntity;
import com.boatchina.imerit.data.entity.BlankEntity;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/2.
 */

public interface UserApi {
    @GET("tp/index.php/user/login")
    Observable<BaseEntity<TokenEntity>> login(@Query("token") String token, @Query("username") String username, @Query("password") String password);

    @GET("tp/index.php/user/register")
    Observable<BaseEntity<TokenEntity>> register(@Query("username") String username, @Query("password") String password,@Query("code") String code,@Query("token") String token);




    @POST("tp/index.php/bind/rel_jpush")
    Observable<BaseEntity<BlankEntity>> rel_jpush(@Query("token") String token, @Query("jpushid") String jpushid);

}
