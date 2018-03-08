package com.boatchina.imerit.app.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.MonitorComponent;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.custom.ImeiSelectView;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.device.DeviceRepo;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MonitorActivity extends NormalActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.isv)
    ImeiSelectView isv;
    @BindView(R.id.edit_imei_phone)
    EditText editImeiPhone;
    @Inject
    DeviceRepo deviceRepo;
    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_monitor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_monitor);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
            return;
        }

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneId = tm.getLine1Number();
        if (phoneId == null) {
            T.showShort(getString(R.string.tip_input_number));
            String phone = PreferencesUtils.getString(this, "phone");
            if(phone!=null) {
                editImeiPhone.setText(phone);
            }

        }else {
            editImeiPhone.setText(phoneId);
        }
        isv.setOnSelect(new ImeiSelectView.OnSelect() {
            @Override
            public void select(String item) {

            }
        });
    }

    @Override
    protected void initializeDi() {
        MonitorComponent monitorComponent = AndroidApplication.getBaseApplication().getApplicationComponent().monitorComponent(getActivityModule());
        monitorComponent.inject(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneId = tm.getLine1Number();
        if (phoneId == null) {
            T.showShort(getString(R.string.tip_input_number));
        }else {
            editImeiPhone.setText(phoneId);
        }
    }

    @OnClick(R.id.btn_call)
    public void onClick() {
        String token = PreferencesUtils.getString(this, "Token");
        String imei = PreferencesUtils.getString(this, "imei");
        PreferencesUtils.putString(this,"phone",editImeiPhone.getText().toString().trim());
        deviceRepo.passivecall(token, imei,editImeiPhone.getText().toString().trim())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        .subscribe(new ProgressSubscriber<>(MonitorActivity.this, new SubscriberOnNextListener<BlankEntity>() {
            @Override
            public void onNext(BlankEntity blank) {
                T.showShort(getString(R.string.tip_wait_monitor));
            }

        }));
    }


}
