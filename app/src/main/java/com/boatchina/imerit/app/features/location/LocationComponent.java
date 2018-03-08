package com.boatchina.imerit.app.features.location;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.data.location.LocationModule;
import com.boatchina.imerit.app.view.activity.GLocationActivity;
import com.boatchina.imerit.data.PerActivity;

import dagger.Subcomponent;

/**
 * Created by fflamingogo on 2016/7/22.
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class, LocationModule.class})
public interface LocationComponent{
    void inject(LocationActivity locationActivity);

    void inject(GLocationActivity GLocationActivity);
}
