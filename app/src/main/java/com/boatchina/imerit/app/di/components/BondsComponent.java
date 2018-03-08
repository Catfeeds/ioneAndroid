package com.boatchina.imerit.app.di.components;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.data.device.DeviceModule;
import com.boatchina.imerit.app.view.activity.FendConfigActivity;
import com.boatchina.imerit.app.view.activity.ImeiInfoActivity;
import com.boatchina.imerit.app.view.activity.ImeiListActivity;
import com.boatchina.imerit.app.view.activity.PhoneActivity;
import com.boatchina.imerit.app.view.activity.SetImeiInfoActivity;
import com.boatchina.imerit.app.view.custom.ImeiSelectView;
import com.boatchina.imerit.app.view.fragment.HomeFragment;
import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.fence.FenceModule;

import dagger.Subcomponent;

/**
 * Created by fflamingogo on 2016/7/22.
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class, DeviceModule.class, FenceModule.class})
public interface BondsComponent{
    void inject(ImeiSelectView imeiSelectView);
    void inject(ImeiListActivity imeiListActivity);
    void inject(ImeiInfoActivity imeiInfoActivity);
    void inject(HomeFragment homeFragment);
    void inject(SetImeiInfoActivity setImeiInfoActivity);
    void inject(PhoneActivity phoneActivity);

    void inject(FendConfigActivity fendConfigActivity);
}
