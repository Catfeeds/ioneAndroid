package com.boatchina.imerit.app.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.view.HomeView;
import com.boatchina.imerit.app.view.activity.InputImeiActivity;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.device.DeviceEntity;
import com.boatchina.imerit.data.device.DeviceRepo;
import com.example.base.YaRxPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fflamingogo on 2016/7/15.
 */
public class HomePresenter extends YaRxPresenter<HomeView> {
    private DeviceRepo deviceRepo;
    @Inject
    public HomePresenter(DeviceRepo deviceRepo) {
        this.deviceRepo=deviceRepo;
    }

    HomeView homeView;

    int id;
    public void setView(@NonNull HomeView homeView) {
        this.homeView = homeView;

    }


    public void navigateTo(int id) {
        loadData();
        this.id = id;

    }

    public void loadData() {
        String token = PreferencesUtils.getString(homeView.context(), "Token");
        token=token==null?"":token;
        deviceRepo.getBonds(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ProgressSubscriber<>(homeView.context(),new SubscriberOnNextListener<List<DeviceEntity>>() {

            @Override
            public void onNext(List<DeviceEntity> bonds) {
                if(bonds.size()==0) {
                    homeView.context().startActivity(new Intent( homeView.context(),InputImeiActivity.class));
                    return;
                }else if(bonds.size()==1) {
                    PreferencesUtils.putString(homeView.context(), "imei",bonds.get(0).getImei());
                }
                homeView.navigateTo(id);
            }
        }));
    }



}
