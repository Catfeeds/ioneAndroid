package com.boatchina.imerit.data.fence;

import com.boatchina.imerit.data.PerActivity;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by fflamingogo on 2016/7/22.
 */
@Module
public class FenceModule {

    @PerActivity
    @Provides
    FenceApi provideFenceApi(final Retrofit retrofit) {
        return retrofit.create(FenceApi.class);
    }
}
