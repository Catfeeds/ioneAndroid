package com.boatchina.imerit.app.features.message;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.device.DeviceModule;

import dagger.Subcomponent;

/**
 * Created by fflamingogo on 2016/8/1.
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class, DeviceModule.class})
public interface InfoComponent{
    void inject(InfoFragment infoFragment);
}
