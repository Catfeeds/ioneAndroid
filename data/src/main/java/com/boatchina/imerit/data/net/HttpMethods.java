package com.boatchina.imerit.data.net;


import com.boatchina.imerit.data.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpMethods {

    public static final String BASE_URL = "https://app.praevolo.xyz/";

    private static final int DEFAULT_TIMEOUT = 5;

    Retrofit retrofit;
    public DataService dataService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request(); //Current Request

                Response response = chain.proceed(originalRequest); //Get response of the request

                /** DEBUG STUFF */
                if (BuildConfig.DEBUG) {
                    //I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body
                    String bodyString = response.body().string();
                    System.out.println(String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
                    System.out.println(String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers()));
                    response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
                }

                return response;
            }
        });


        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        dataService = retrofit.create(DataService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public DataService getService() {
        return retrofit.create(DataService.class);
    }


}
