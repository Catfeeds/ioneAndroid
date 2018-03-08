package com.boatchina.imerit.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.BondsComponent;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.device.DeviceEntity;
import com.boatchina.imerit.data.device.DeviceRepo;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SetImeiInfoActivity extends NormalActivity {

    @BindView(R.id.tv_imei)
    TextView tvImei;
    @BindView(R.id.edit_imei_name)
    EditText editImeiName;
    @BindView(R.id.edit_imei_phone)
    EditText editImeiPhone;
    @Inject
    DeviceRepo deviceRepo;
    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_set_username);
    }

    String tag;

    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_imei_info);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        imei = getIntent().getStringExtra("imei");

        tag = getIntent().getStringExtra("tag");
        tvImei.setText(getString(R.string.desc_imei)+":" + imei);

        String token = PreferencesUtils.getString(this, "Token");
        deviceRepo.deviceGetInfo(token,imei)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ProgressSubscriber<>(SetImeiInfoActivity.this, new SubscriberOnNextListener<DeviceEntity>() {
            @Override
            public void onNext(DeviceEntity bond) {
                editImeiName.setText(bond.getName());
                editImeiPhone.setText(bond.getPhone());
            }
        }));

    }

    @Override
    protected void initializeDi() {
        BondsComponent bondsComponent = AndroidApplication.getBaseApplication().getApplicationComponent().bondsComponent(getActivityModule());
        bondsComponent.inject(this);
    }


    @OnClick(R.id.btn_commit)
    public void onClick() {
        String token = PreferencesUtils.getString(this, "Token");
        deviceRepo.deviceSetInfo(token, imei, editImeiName.getText().toString().trim(),editImeiPhone.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ProgressSubscriber<>(this, new SubscriberOnNextListener<BlankEntity>() {

            @Override
            public void onNext(BlankEntity blank) {
                if(tag.equals("info")) {
                    finish();
                }else {
                    startActivity(new Intent(SetImeiInfoActivity.this, TabActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        }));

    }

}
