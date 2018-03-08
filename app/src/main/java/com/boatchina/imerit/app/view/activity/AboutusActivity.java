package com.boatchina.imerit.app.view.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.boatchina.imerit.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutusActivity extends NormalActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_aboutus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_aboutus);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        try {
            tvVersion.setText(getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initializeDi() {

    }

    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

}
