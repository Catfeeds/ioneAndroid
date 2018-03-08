package com.boatchina.imerit.app.features.history;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.data.location.LocationModule;
import com.boatchina.imerit.app.view.activity.GHistoryActivity;
import com.boatchina.imerit.data.PerActivity;

import dagger.Subcomponent;

/**
 * Created by fflamingogo on 2016/8/1.
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class, LocationModule.class})
public interface HistoryComponent{
    void inject(HistoryActivity historyActivity);

    void inject(GHistoryActivity gHistoryActivity);
}
