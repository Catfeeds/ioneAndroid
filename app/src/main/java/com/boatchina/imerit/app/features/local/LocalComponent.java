package com.boatchina.imerit.app.features.local;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.location.LocationModule;

import dagger.Subcomponent;

/**
 * Created by Administrator on 2017/3/27.
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class, LocationModule.class})
public interface LocalComponent {
    void inject(LocalFragment localFragment);
}
