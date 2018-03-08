package com.boatchina.imerit.app.features.location;

import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.view.activity.login.MapStatus;
import com.boatchina.imerit.app.view.activity.login.MyLatLng;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.location.LocationEntity;
import com.boatchina.imerit.data.location.LocationRepo;
import com.example.base.YaRxPresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fflamingogo on 2016/8/1.
 */
public class LocationPresenter extends YaRxPresenter<LocationView> {

    private LocationRepo locationRepo;


    private  MapStatus mapStatus;
    @Inject
    LocationPresenter(LocationRepo locationRepo) {
        this.locationRepo =locationRepo;
    }

    public void setStatus(MapStatus mapStatus) {
        this.mapStatus = mapStatus;

    }

    public void showLocation() {
        String token = PreferencesUtils.getString(getView().context(), "Token");
        token = token==null?"":token;
        String imei = PreferencesUtils.getString(getView().context(), "imei");
        imei = imei==null?"":imei;
        addUtilDestroy(
                locationRepo.location(token,imei,"gcj02")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber(getView().context(),subscriberOnNextListener))
        );


    }
    public void selectLocation(String imei) {
        String token = PreferencesUtils.getString(getView().context(), "Token");
        token = token==null?"":token;
        imei = imei==null?"":imei;
        addUtilDestroy(
                locationRepo.location(token,imei,"gcj02")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber(getView().context(),subscriberOnNextListener ))
        );
    }


    private SubscriberOnNextListener subscriberOnNextListener =new  SubscriberOnNextListener<LocationEntity>() {

        @Override
        public void onNext(LocationEntity location) {
            MyLatLng myLatLng = mapStatus.data2Map(location);
            getView().addOnMap(myLatLng.getLat(),myLatLng.getLng(),myLatLng.getTitle());
        }

    };




}
