/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.boatchina.imerit.app.di.components;

import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.app.di.modules.ApplicationModule;
import com.boatchina.imerit.app.di.modules.ProviderModule;
import com.boatchina.imerit.app.features.history.HistoryComponent;
import com.boatchina.imerit.app.features.local.LocalComponent;
import com.boatchina.imerit.app.features.location.LocationComponent;
import com.boatchina.imerit.app.features.login.UserComponent;
import com.boatchina.imerit.app.features.message.InfoComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {ApplicationModule.class,ProviderModule.class})
public interface ApplicationComponent {
MyReceiverComponent myReceiverComponent();
TalkComponent talkComponent();
LocationComponent locationComponent();
UserComponent userComponent(ActivityModule activityModule);
BondsComponent bondsComponent(ActivityModule activityModule);
FenceComponent fenceComponent(ActivityModule activityModule);
  HistoryComponent historyComponent(ActivityModule activityModule);
  InfoComponent infoComponent(ActivityModule activityModule);
  MonitorComponent monitorComponent(ActivityModule activityModule);
  ScanComponent scanComponent(ActivityModule activityModule);
  TabComponent tabComponent(ActivityModule activityModule);
  LocalComponent localComponent(ActivityModule activityModule);
}
