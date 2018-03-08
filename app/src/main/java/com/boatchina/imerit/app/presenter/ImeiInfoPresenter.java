package com.boatchina.imerit.app.presenter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.view.ImeiInfoView;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.device.DeviceRepo;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.device.DeviceEntity;
import com.example.base.YaRxPresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fflamingogo on 2016/8/11.
 */
public class ImeiInfoPresenter extends YaRxPresenter<ImeiInfoView> {



    private int index;
    @Inject
    DeviceRepo deviceRepo;
    private String mImei;
    @Inject
    public ImeiInfoPresenter(DeviceRepo deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    public void loadData(final String imei) {
        String token = PreferencesUtils.getString(getView().context(), "Token");
        addUtilDestroy(deviceRepo.deviceGetInfo(token,imei)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber<>(getView().context(), new SubscriberOnNextListener<DeviceEntity> (){

                    @Override
                    public void onNext(DeviceEntity bond) {
                        getView().showData(bond);
                    }
                })))
        ;
    }

    public void unbind (final String imei) {
        this.mImei = imei;
        String token = PreferencesUtils.getString(getView().context(), "Token");
        addUtilDestroy(deviceRepo.unBind(token,imei)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber<>(getView().context(), new SubscriberOnNextListener<BlankEntity>() {
                    @Override
                    public void onNext(BlankEntity blank) {
                        String imei = PreferencesUtils.getString(getView().context(), "imei");
                        if(ImeiInfoPresenter.this.mImei.equals(imei)) {
                            PreferencesUtils.putString(getView().context(),"imei","");
                        }
                        getView().unBindNext();
                    }
                })));
    }


    public void showChooseTimeDialog(final String imei) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getView().context());  //先得到构造器
        builder.setTitle(R.string.title_select_mode);                                     //设置标题
        final String[] array = new String[]{getView().context().getString(R.string.desc_normal_mode),getView().context().getString(R.string.desc_less_electricity_mode) };
        final int[] result = new int[]{60,600};

        builder.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                index =which;
            }
        });
        builder.setPositiveButton(R.string.desc_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String token = PreferencesUtils.getString(getView().context(), "Token");
                addUtilDestroy(deviceRepo.Config(token,imei,result[index])
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new ProgressSubscriber<>(getView().context(), new SubscriberOnNextListener<BlankEntity>() {
                            @Override
                            public void onNext(BlankEntity blank) {
                                getView().changeTime(array[index]);
                            }

                        })))
                ;
            }
        });
        builder.create().show();
    }
}
