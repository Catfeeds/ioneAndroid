package com.boatchina.imerit.data.device;

import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.device.DeviceApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by fflamingogo on 2016/7/22.
 */
@Module
public class DeviceModule {



    @PerActivity
    @Provides
    DeviceApi provideDeviceApi(final Retrofit retrofit) {
        return retrofit.create(DeviceApi.class);
    }
}
