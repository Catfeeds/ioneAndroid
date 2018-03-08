package com.boatchina.imerit.app.di.modules;

import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.net.DataService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by fflamingogo on 2016/8/1.
 */
@Module
public class ConfModule {




    @PerActivity
    @Provides
    DataService provideGithubAPI(final Retrofit retrofit) {
        return retrofit.create(DataService.class);
    }
}
