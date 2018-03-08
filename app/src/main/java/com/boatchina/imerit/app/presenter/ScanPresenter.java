package com.boatchina.imerit.app.presenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import com.boatchina.imerit.app.Constants;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.ScanView;
import com.boatchina.imerit.data.ErrorProgressSubscriber;
import com.boatchina.imerit.data.MySubscriberOnNextListener;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.device.DeviceRepo;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.example.base.YaRxPresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fflamingogo on 2016/8/3.
 */
public class ScanPresenter extends YaRxPresenter<ScanView> {

    private String imei;

    private DeviceRepo deviceRepo;

    @Inject
    public ScanPresenter(DeviceRepo deviceRepo) {
        this.deviceRepo = deviceRepo;
    }



    public void bind (final String token1,final String token2,final String imei,final String nick) {
        this.imei =imei;
        addUtilStop(deviceRepo.Bind(token1, token2, imei, nick)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorProgressSubscriber<>(getView().context(), new MySubscriberOnNextListener<BlankEntity>() {
                    @Override
                    public void onError(Throwable e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getView().context());
                        final EditText editText = new EditText(getView().context());
                        editText.setHint("消息验证");
                        builder.setView(editText).setTitle("是否要发送请求");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final String token = PreferencesUtils.getString(getView().context(), "Token");
                                        addUtilStop(deviceRepo.bindreq(token, Constants.APP_TOKEN, imei, editText.getText().toString().trim())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new ProgressSubscriber<BlankEntity>(getView().context(), new SubscriberOnNextListener<BlankEntity>() {

                                                    @Override
                                                    public void onNext(BlankEntity blank) {
                                                        T.showShort("已经发送绑定申请");
                                                    }
                                                })));

                                    }
                                })
                                .show();


                    }

                    @Override
                    public void onNext(BlankEntity blank) {
                        getView().scanAndBind(imei);
                    }


                }))
        );
    }






}
