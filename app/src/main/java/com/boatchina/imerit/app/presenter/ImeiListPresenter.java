package com.boatchina.imerit.app.presenter;

import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.view.ImeiListView;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.device.DeviceRepo;
import com.boatchina.imerit.data.device.DeviceEntity;
import com.boatchina.imerit.data.fence.FenceEntity;
import com.boatchina.imerit.data.fence.FenceRepo;
import com.example.base.YaRxPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fflamingogo on 2016/8/11.
 */
public class ImeiListPresenter extends YaRxPresenter<ImeiListView> {

    @Inject
    DeviceRepo deviceRepo;
    @Inject
    FenceRepo fenceRepo;

    @Inject
    public ImeiListPresenter() {
    }

    public void getImeilist() {
        String token = PreferencesUtils.getString(getView().context(), "Token");
        addUtilDestroy(deviceRepo.getBonds(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber<>(getView().context(), new SubscriberOnNextListener<List<DeviceEntity>>() {
                    @Override
                    public void onNext(List<DeviceEntity> bonds) {
                        getView().showImeiList(bonds);
                    }
                })))
        ;
    }
    public void addFend(String imei,String name ,FenceEntity fend){
        String token = PreferencesUtils.getString(getView().context(), "Token");
        addUtilDestroy(fenceRepo.addFine(token, imei, name, "circle", fend.getLng1() + "", fend.getLat1() + "", "", "",fend.getRadius()+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<FenceEntity>(getView().context(), new SubscriberOnNextListener<FenceEntity>() {


                    @Override
                    public void onNext(FenceEntity fenceEntity) {
                        getView().addFendNext();
                    }
                })))
        ;
    }
    public void updateFend(int fine,String imei,String name ,FenceEntity fend){
        String token = PreferencesUtils.getString(getView().context(), "Token");
        addUtilDestroy(fenceRepo.updatefine(fine,token, imei, name, "circle", fend.getLng1() + "", fend.getLat1() + "", "", "",fend.getRadius()+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<FenceEntity>(getView().context(), new SubscriberOnNextListener<FenceEntity>() {


                    @Override
                    public void onNext(FenceEntity fenceEntity) {
                        getView().addFendNext();
                    }
                })))
        ;
    }





}
