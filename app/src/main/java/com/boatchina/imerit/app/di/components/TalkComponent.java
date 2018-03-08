package com.boatchina.imerit.app.di.components;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.app.view.talk.TalkActivity;
import com.boatchina.imerit.data.PerActivity;

import dagger.Subcomponent;

/**
 * Created by fflamingogo on 2016/7/22.
 */
@PerActivity
@Subcomponent(modules = {ActivityModule.class})
public interface TalkComponent{
    void inject(TalkActivity talkActivity);
}
