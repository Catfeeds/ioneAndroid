package com.boatchina.imerit.app.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.BondsComponent;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.view.custom.ImeiSelectView;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.device.DeviceEntity;
import com.boatchina.imerit.data.device.DeviceRepo;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PhoneActivity extends NormalActivity {

    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @BindView(R.id.isv)
    ImeiSelectView isv;



    @Inject
    DeviceRepo deviceRepo;
    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_phone);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        String token = PreferencesUtils.getString(this, "Token");
        String imei = PreferencesUtils.getString(this, "imei");
        deviceRepo.deviceGetInfo(token, imei)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ProgressSubscriber<>(PhoneActivity.this, new SubscriberOnNextListener<DeviceEntity>() {
            @Override
            public void onNext(DeviceEntity bond) {
                tvPhone.setText(getString(R.string.desc_number)+":" + bond.getPhone());
            }

        }));
        isv.setOnSelect(new ImeiSelectView.OnSelect() {
            @Override
            public void select(String item) {
                String token = PreferencesUtils.getString(PhoneActivity.this, "Token");
                deviceRepo.deviceGetInfo(token, item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(PhoneActivity.this, new SubscriberOnNextListener<DeviceEntity>() {
                    @Override
                    public void onNext(DeviceEntity bond) {
                        tvPhone.setText(getString(R.string.desc_number)+":" + bond.getPhone());
                    }

                }));
            }
        });


    }

    @Override
    protected void initializeDi() {
        BondsComponent bondsComponent = AndroidApplication.getBaseApplication().getApplicationComponent().bondsComponent(getActivityModule());
        bondsComponent.inject(this);
    }

    @OnClick(R.id.btn_call)
    public void onClick() {
        Intent intent = new Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:" + tvPhone.getText().toString().trim())
        );
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
            return;
        }
        startActivity(intent);

    }

}
