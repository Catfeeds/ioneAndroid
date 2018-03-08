package com.boatchina.imerit.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.BondsComponent;
import com.boatchina.imerit.app.presenter.ImeiListPresenter;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.ImeiListView;
import com.boatchina.imerit.app.view.adapter.ImeiCheckAdapter;
import com.boatchina.imerit.data.device.DeviceEntity;
import com.boatchina.imerit.data.fence.FenceEntity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


public class FendConfigActivity extends BaseActivity<ImeiListView,ImeiListPresenter,BondsComponent> implements ImeiListView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @Inject
    ImeiCheckAdapter imeiCheckAdapter;


    FenceEntity fend;
    @BindView(R.id.edit_name)
    EditText editName;

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_config);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fend_config);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        fend = (FenceEntity) getIntent().getSerializableExtra("fend");
        editName.setText(fend==null?"":fend.getName());
        mPresenter.getImeilist();
        setupRecyclerview();
    }

    @Override
    protected BondsComponent initializeDi() {
        BondsComponent bondsComponent =  AndroidApplication.getBaseApplication().getApplicationComponent().bondsComponent(getActivityModule());


        return bondsComponent;
    }

    @Override
    protected void injectDependencies(BondsComponent component) {
        component.inject(this);
    }

    private void setupRecyclerview() {
        recyclerview.setAdapter(imeiCheckAdapter);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        imeiCheckAdapter.setOnItemClickListener(new ImeiCheckAdapter.OnItemClickListener() {
            @Override
            public void onImeiItemClicked(DeviceEntity bond) {

            }
        });
    }

    @Override
    public void showImeiList(List<DeviceEntity> imeiList) {
        imeiCheckAdapter.setImeiCheckList(fend.getDevices());
        imeiCheckAdapter.setImeiList(imeiList);
    }

    @Override
    public void addFendNext() {
        finish();
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.getItem(0).setTitle(getString(R.string.desc_commit));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                if(editName.getText().toString().trim().equals("")) {
                    T.showShort("围栏名称不能为空");
                    return super.onOptionsItemSelected(item);
                }
                Observable.from(imeiCheckAdapter.getImeiList())
                        .filter(new Func1<DeviceEntity, Boolean>() {
                            @Override
                            public Boolean call(DeviceEntity bond) {
                                return bond.isChecked();
                            }
                        })

                        .map(new Func1<DeviceEntity, String>() {
                            @Override
                            public String call(DeviceEntity bond) {
                                return bond.getImei();
                            }
                        })
                        .toList()
                        .subscribe(new Subscriber<List<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                T.showShort("请勾选设备");
                            }

                            @Override
                            public void onNext(List<String> strings) {
                                String s = "";
                                if(strings.size()!=0) {
                                    for (String string : strings) {
                                        s+=string+",";
                                    }
                                }

                                System.out.println("..." + strings.toString().trim());
                                if(fend.getId()==0) {
                                    mPresenter.addFend(s.substring(0, s.length() - 1), editName.getText().toString().trim(), fend);
                                }else {
                                    mPresenter.updateFend(fend.getId(),s.substring(0, s.length() - 1), editName.getText().toString().trim(), fend);
                                }
                            }
                        });


                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
