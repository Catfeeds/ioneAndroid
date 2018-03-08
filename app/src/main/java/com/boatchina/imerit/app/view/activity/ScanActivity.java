package com.boatchina.imerit.app.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.Constants;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.ScanComponent;
import com.boatchina.imerit.app.presenter.ScanPresenter;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.ScanView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends BaseActivity<ScanView,ScanPresenter,ScanComponent> implements QRCodeView.Delegate, ScanView {
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @BindView(R.id.zxingview)
    ZXingView zxingview;

    String nick;
    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_scan);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);


        nick = getIntent().getStringExtra("nick");

        zxingview.setDelegate(this);
        zxingview.startSpot();
    }

    @Override
    protected ScanComponent initializeDi() {
        ScanComponent scanComponent = AndroidApplication.getBaseApplication().getApplicationComponent().scanComponent(getActivityModule());
        return scanComponent;
    }

    @Override
    protected void injectDependencies(ScanComponent component) {
        component.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        zxingview.startCamera();
    }

    @Override
    public void onStop() {
        zxingview.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        zxingview.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        String token = PreferencesUtils.getString(ScanActivity.this, "Token");
        mPresenter.bind(token, Constants.APP_TOKEN,result,nick);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        zxingview.showScanRect();

        if (requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            String picturePath;
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                picturePath = c.getString(columnIndex);
                c.close();
            } catch (Exception e) {
                picturePath = data.getData().getPath();
            }

            if (new File(picturePath).exists()) {
                QRCodeDecoder.decodeQRCode(BitmapFactory.decodeFile(picturePath), new QRCodeDecoder.Delegate() {
                    @Override
                    public void onDecodeQRCodeSuccess(String result) {
                        String token = PreferencesUtils.getString(ScanActivity.this, "Token");
                        mPresenter.bind(token,Constants.APP_TOKEN, result,nick);
                        Toast.makeText(ScanActivity.this, result, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDecodeQRCodeFailure() {
                        Toast.makeText(ScanActivity.this, R.string.tip_not_found_code, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }



    @Override
    public void showError(String message) {
        finish();
        T.showShort(message);
    }

    @Override
    public Context context() {
        return this;
    }


    @OnClick({R.id.choose_qrcde_from_gallery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_qrcde_from_gallery:
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }

    @Override
    public void scanAndBind(String result) {
        PreferencesUtils.putString(this, "imei", result);
        startActivity(new Intent(ScanActivity.this,SetImeiInfoActivity.class).putExtra("imei",result));
        finish();
        vibrate();
    }

}