package com.boatchina.imerit.data.net;

import com.boatchina.imerit.data.entity.BaseEntity;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.entity.ContactEntity;
import com.boatchina.imerit.data.entity.LaglngEntity;
import com.boatchina.imerit.data.entity.TimeConfigEntity;
import com.boatchina.imerit.data.entity.UsersEntity;
import com.boatchina.imerit.data.entity.VoiceEntity;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface DataService {



    @GET("tp/index.php/user/get_verify_code")
    Observable<BaseEntity<BlankEntity>> getverifycode(@Query("phone") String phone,@Query("token") String token);



    @POST("tp/index.php/bind/getbondedusers")
    Observable<BaseEntity<List<UsersEntity>>> getbondedusers(@Query("token") String token, @Query("imei") String imei);


    @POST("tp/index.php/bind/bindmaster")
    Observable<BaseEntity<BlankEntity>> bindmaster(@Query("token") String token, @Query("imei") String imei,@Query("user") String user);


    @Multipart
    @POST("zlt/api/upload.php")
    Call<ResponseBody> uploadFile(@Query("token") String token,@Part MultipartBody.Part file);


    @POST("tp/index.php/voice/PostVoice")
    Observable<BaseEntity<List<VoiceEntity>>> postvoice(@Query("token") String token, @Query("imei") String imei, @Query("url") String url);


    @POST("tp/index.php/pb/GetDevicePB")
    Observable<BaseEntity<List<ContactEntity>>> getdevicepb(@Query("token") String token, @Query("imei") String imei);

    @POST("tp/index.php/pb/SetDevicePB")
    Observable<BaseEntity<List<Integer>>> setdevicepb(@Query("token") String token, @Query("imei") String imei, @Query("indexes") String indexes,@Query("numbers") String numbers,@Query("names") String names);

    @POST("tp/index.php/device/getdevicend")
    Observable<BaseEntity<List<TimeConfigEntity>>> getdevicend(@Query("token") String token, @Query("imei") String imei);

    @POST("tp/index.php/device/setdevicend")
    Observable<BaseEntity<List<Integer>>> setdevicend(@Query("token") String token, @Query("imei") String imei, @Query("indexes") String indexes,@Query("begins") String begins,@Query("ends") String ends,@Query("repeats") String repeats);


    @GET("zlt/api/convert.php")
    Observable<BaseEntity<LaglngEntity>> convert(@Query("from") String from, @Query("to") String to, @Query("lat") double lat, @Query("lng") double lng);

}
