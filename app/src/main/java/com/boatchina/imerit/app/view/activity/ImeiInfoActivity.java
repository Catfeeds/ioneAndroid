package com.boatchina.imerit.app.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.BondsComponent;
import com.boatchina.imerit.app.presenter.ImeiInfoPresenter;
import com.boatchina.imerit.app.view.ImeiInfoView;
import com.boatchina.imerit.data.device.DeviceEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImeiInfoActivity extends BaseActivity<ImeiInfoView,ImeiInfoPresenter,BondsComponent> implements ImeiInfoView {

    @BindView(R.id.tv_imei)
    TextView tvImei;
    DeviceEntity bond;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_device_info);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_imei_info);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        bond = (DeviceEntity) getIntent().getSerializableExtra("bond");



    }

    @Override
    protected BondsComponent initializeDi() {
        BondsComponent bondsComponent = AndroidApplication.getBaseApplication().getApplicationComponent().bondsComponent(getActivityModule());
        return bondsComponent;
    }

    @Override
    protected void injectDependencies(BondsComponent component) {
        component.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadData(bond.getImei());
    }


    @OnClick({R.id.fl_imei, R.id.fl_name, R.id.fl_time, R.id.fl_phone,R.id.fl_master,R.id.fl_contants,R.id.fl_disturb,R.id.btn_unbind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_imei:
                break;
            case R.id.fl_phone:
            case R.id.fl_name:
//                EditText editText = new EditText(ImeiInfoActivity.this);
//                new AlertDialog.Builder(ImeiInfoActivity.this)
//                .setView(editText)
//                        .setTitle("xxx")
//                        .setMessage("xxx")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                 .show();

                startActivity(new Intent(ImeiInfoActivity.this, SetImeiInfoActivity.class).putExtra("imei", ImeiInfoActivity.this.bond.getImei()).putExtra("tag","info"));
                break;
            case R.id.fl_time:
                mPresenter.showChooseTimeDialog(bond.getImei());
                break;
            case R.id.fl_master:
                startActivity(new Intent(ImeiInfoActivity.this,UsersListActivity.class).putExtra("imei", ImeiInfoActivity.this.bond.getImei()));
                break;
            case R.id.fl_contants:
                startActivity(new Intent(ImeiInfoActivity.this,ContantsActivity.class).putExtra("imei", ImeiInfoActivity.this.bond.getImei()));
                break;
            case R.id.fl_disturb:
                startActivity(new Intent(ImeiInfoActivity.this,DisturbActivity.class).putExtra("imei", ImeiInfoActivity.this.bond.getImei()));
                break;
            case R.id.btn_unbind:
                AlertDialog.Builder builder = new AlertDialog.Builder(ImeiInfoActivity.this);
                builder.setMessage(R.string.desc_device_unbind)
                        .setPositiveButton(R.string.desc_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.unbind(bond.getImei());
                            }
                        })
                        .setNegativeButton(R.string.desc_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }


    @Override
    public Context context() {
        return this;
    }

    @Override
    public void unBindNext() {
        finish();
    }

    @Override
    public void showData(final DeviceEntity device) {
        tvImei.setText(this.bond.getImei());

        tvName.setText(device.getName() == null ? getString(R.string.desc_no_set) : device.getName());
        if (device.getInterval() == 60) {
            tvTime.setText(R.string.desc_normal_mode);
        } else {
            tvTime.setText(R.string.desc_less_electricity_mode);
        }
        tvPhone.setText(device.getPhone());

    }

    @Override
    public void changeTime(String time) {
        tvTime.setText(time);
    }


}
