package com.boatchina.imerit.app.di.modules;

import com.boatchina.imerit.data.BuildConfig;
import com.boatchina.imerit.data.entity.DbOpenHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;

import static com.boatchina.imerit.data.net.HttpMethods.BASE_URL;

/**
 * Created by Administrator on 2017/2/28.
 */
@Module
public class ProviderModule {
    @Singleton
    @Provides
    public BriteDatabase provideBriteDb(DbOpenHelper dbOpenHelper) {
        final BriteDatabase briteDb =
                SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, AndroidSchedulers.mainThread());
        briteDb.setLoggingEnabled(true);
        return briteDb;
    }
    private static final boolean DEBUG = "debug".equals(BuildConfig.BUILD_TYPE);


    @Singleton
    @Provides
    OkHttpClient provideHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request(); //Current Request

                Response response = chain.proceed(originalRequest); //Get response of the request

                /** DEBUG STUFF */
                if (com.boatchina.imerit.data.BuildConfig.DEBUG) {
                    //I am logging the response body in debug mode. When I do this I consume the response (OKHttp only lets you do this once) so i have re-build a new one using the cached body
                    String bodyString = response.body().string();
                    System.out.println(String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
                    System.out.println(String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers()));
                    response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
                }

                return response;
            }
        });


        return builder.build();
    }


    @Singleton
    @Provides
    Retrofit provideRetrofit(final OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }


    @Singleton
    @Provides
    Gson provideGson() {
        final GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
}
