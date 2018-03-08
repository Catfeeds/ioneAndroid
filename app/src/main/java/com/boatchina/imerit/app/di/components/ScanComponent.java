package com.boatchina.imerit.app.di.components;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.data.device.DeviceModule;
import com.boatchina.imerit.app.view.activity.InputImeiActivity;
import com.boatchina.imerit.app.view.activity.ScanActivity;
import com.boatchina.imerit.data.PerActivity;

import dagger.Subcomponent;

/**
 * Created by fflamingogo on 2016/7/22.
 */
@PerActivity
@Subcomponent( modules = {ActivityModule.class, DeviceModule.class})
public interface ScanComponent{
    void inject(ScanActivity scanActivity);
    void inject(InputImeiActivity inputImeiActivity);
}
