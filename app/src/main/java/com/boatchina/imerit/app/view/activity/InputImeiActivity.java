package com.boatchina.imerit.app.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.Constants;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.ScanComponent;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.data.ErrorProgressSubscriber;
import com.boatchina.imerit.data.MySubscriberOnNextListener;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.device.DeviceRepo;
import com.boatchina.imerit.data.entity.BlankEntity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InputImeiActivity extends NormalActivity {

    @BindView(R.id.edit_imei)
    EditText editImei;

    @BindView(R.id.tv_nick)
    TextView tvNick;

    @Inject
    DeviceRepo deviceRepo;
    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_device_bind);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_input_imei);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

    }

    @Override
    protected void initializeDi() {
        ScanComponent scanComponent = AndroidApplication.getBaseApplication().getApplicationComponent().scanComponent(getActivityModule());

        scanComponent.inject(this);
    }

    @OnClick({R.id.btn_scan, R.id.btn_commit_imei,R.id.ll_nick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                startActivity(new Intent(InputImeiActivity.this, ScanActivity.class).putExtra("nick",tvNick.getText().toString().trim()));
                finish();
                break;
            case R.id.btn_commit_imei:
                if (editImei.getText().toString().trim().equals("")) {

                    T.showShort(getString(R.string.tip_input_imei));
                    return;
                }
                final String token = PreferencesUtils.getString(InputImeiActivity.this, "Token");
                deviceRepo.Bind(token, Constants.APP_TOKEN, editImei.getText().toString().trim(), tvNick.getText().toString().trim())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ErrorProgressSubscriber<BlankEntity>(InputImeiActivity.this, new MySubscriberOnNextListener<BlankEntity>() {
                            @Override
                            public void onError(Throwable e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InputImeiActivity.this);
                                final EditText editText = new EditText(InputImeiActivity.this);
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
                                                deviceRepo.bindreq(token, Constants.APP_TOKEN, editImei.getText().toString().trim(), editText.getText().toString().trim())
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new ProgressSubscriber<>(InputImeiActivity.this, new SubscriberOnNextListener<BlankEntity>() {

                                                    @Override
                                                    public void onNext(BlankEntity blank) {
                                                        T.showShort("已经发送绑定申请");
                                                    }
                                                }));
                                            }
                                        })
                                        .show();


                            }

                            @Override
                            public void onNext(BlankEntity blank) {
                                editImei.setText(editImei.getText().toString().trim());
                                PreferencesUtils.putString(InputImeiActivity.this, "imei", editImei.getText().toString().trim());
                                startActivity(new Intent(InputImeiActivity.this, SetImeiInfoActivity.class).putExtra("imei", editImei.getText().toString().trim()));
                                finish();
                            }


                        }));
                break;
            case R.id.ll_nick:
                AlertDialog.Builder builder = new AlertDialog.Builder(InputImeiActivity.this);  //先得到构造器
                builder.setTitle("选择本人身份");
                final String[] array = new String[]{"爸爸","妈妈","爷爷","奶奶","哥哥","姐姐"};
                builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tvNick.setText(array[which]);
                    }
                });
                builder.create().show();
                break;
        }
    }


}
