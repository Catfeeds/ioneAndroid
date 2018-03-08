package com.boatchina.imerit.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.BondsComponent;
import com.boatchina.imerit.app.presenter.ImeiListPresenter;
import com.boatchina.imerit.app.view.ImeiListView;
import com.boatchina.imerit.app.view.adapter.ImeiAdapter;
import com.boatchina.imerit.data.device.DeviceEntity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImeiListActivity extends BaseActivity<ImeiListView,ImeiListPresenter,BondsComponent> implements ImeiListView {

    @BindView(R.id.rv_imei)
    RecyclerView rvImei;

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_device_list);
    }

    @Inject
    ImeiAdapter imeiAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_imei_list);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


        setupRecyclerview();



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
        mPresenter.getImeilist();
    }

    private void setupRecyclerview() {
        rvImei.setAdapter(imeiAdapter);
        rvImei.setLayoutManager(new LinearLayoutManager(this));
        imeiAdapter.setOnItemClickListener(new ImeiAdapter.OnItemClickListener() {
            @Override
            public void onImeiItemClicked(DeviceEntity bond) {
                startActivity(new Intent(ImeiListActivity.this,ImeiInfoActivity.class).putExtra("bond",bond));
            }
        });
    }



    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showImeiList(List<DeviceEntity> imeiList) {
        imeiAdapter.setImeiList(imeiList);
    }

    @Override
    public void addFendNext() {

    }


}
