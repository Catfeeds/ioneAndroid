package com.boatchina.imerit.app.di.components;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.app.view.activity.TabActivity;
import com.boatchina.imerit.data.PerActivity;

import dagger.Subcomponent;

/**
 * Created by fflamingogo on 2016/8/1.
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class})
public interface TabComponent{
    void inject(TabActivity tabActivity);
}
