package com.boatchina.imerit.app.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.ApplicationComponent;
import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.app.features.login.LoginActivity;
import com.boatchina.imerit.data.MessageEvent;
import com.example.base.NormalDiActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Base {@link Activity} class for every Activity in this application.
 */
 public abstract class NormalActivity extends NormalDiActivity {
  ProgressDialog dialog;
//  @Inject
//  Navigator navigator;
Toolbar mToolbar;
  protected abstract String getToolbarTitle();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    this.getApplicationComponent().inject(this);

      initToolbar();
  }

  public void mShowLoading() {
    dialog = new ProgressDialog(this);
    dialog.setMessage("loading");

    dialog.setCancelable(false);
    dialog.show();
  }

  public void mHideLoading() {
    dialog.dismiss();
  }

  @Override
  public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }
  protected void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
  }
  protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
  }
  @Override
  protected void onDestroy() {
    super.onDestroy();

  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(MessageEvent event) {
    System.out.println("=======");
    Intent intent = new Intent(this,LoginActivity.class);
    startActivity(intent);
  }
  /**
   * Adds a {@link Fragment} to this activity's layout.
   *
   * @param containerViewId The container view to where add the fragment.
   * @param fragment The fragment to be added.
   */
  protected void addFragment(int containerViewId, Fragment fragment) {
    FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(containerViewId, fragment);
    fragmentTransaction.commit();
  }


  public ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication)getApplication()).getApplicationComponent();
  }


  public ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }

  public void initToolbar() {

    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    if(mToolbar!=null) {
      if(getToolbarTitle().equals(getResources().getString(R.string.app_name))) {//等于appname的时候是主页
        mToolbar.setTitle(getToolbarTitle());
        setSupportActionBar(mToolbar);
      }else {
        mToolbar.setTitle(getToolbarTitle());
        setSupportActionBar(mToolbar);
      getSupportActionBar().setHomeButtonEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      }

    }

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch(id){
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }


}
