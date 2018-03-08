package com.boatchina.imerit.app.features.local;

import com.boatchina.imerit.data.location.LocationRepo;
import com.example.base.YaPresenter;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/3/27.
 */

public class LocalPresenter extends YaPresenter<LocalView> {

    LocationRepo locationRepo;
    @Inject
    public LocalPresenter(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }



}
