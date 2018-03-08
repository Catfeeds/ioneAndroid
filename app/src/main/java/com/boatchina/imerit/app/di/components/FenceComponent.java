package com.boatchina.imerit.app.di.components;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.app.view.activity.FenceActivity;
import com.boatchina.imerit.app.view.activity.FenceListActivity;
import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.fence.FenceModule;

import dagger.Subcomponent;

/**
 * Created by fflamingogo on 2016/7/22.
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class, FenceModule.class})
public interface FenceComponent{
    void inject(FenceActivity fenceActivity);

    void inject(FenceListActivity fenceListActivity);
}
