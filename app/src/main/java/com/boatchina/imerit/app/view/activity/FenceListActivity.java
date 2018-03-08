package com.boatchina.imerit.app.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.FenceComponent;
import com.boatchina.imerit.app.presenter.FenceListPresenter;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.view.FendListView;
import com.boatchina.imerit.app.view.adapter.FenceAdapter;
import com.boatchina.imerit.data.fence.FenceEntity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FenceListActivity extends BaseActivity<FendListView,FenceListPresenter,FenceComponent> implements FendListView {
    @BindView(R.id.rv_fend)
    RecyclerView rvFend;

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_fence_list);
    }
    @Inject
    FenceAdapter fenceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fend_list);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupRecyclerview();
//        fenceListPresenter.loadFendList();

    }

    @Override
    protected FenceComponent initializeDi() {
        FenceComponent fenceComponent =  AndroidApplication.getBaseApplication().getApplicationComponent().fenceComponent(getActivityModule());
        return fenceComponent;
    }

    @Override
    protected void injectDependencies(FenceComponent component) {
        component.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPresenter !=null){
            mPresenter.loadFendList();
        }
    }

    private void setupRecyclerview() {
        rvFend.setAdapter(fenceAdapter);
        rvFend.setLayoutManager(new LinearLayoutManager(this));
        fenceAdapter.setOnItemClickListener(new FenceAdapter.OnItemClickListener() {
            @Override
            public void onImeiItemClicked(FenceEntity fend) {
//                String map = PreferencesUtils.getString(FenceListActivity.this, "map");
//                if(map.equals("Google Map")) {
//                            startActivity(new Intent(FenceListActivity.this, GFendActivity.class).putExtra("fend",fend));
//                }else if(map.equals("高德地图")){
                            startActivity(new Intent(FenceListActivity.this, NewFendActivity.class).putExtra("fend",fend));
//                }
            }

            @Override
            public void onImeiItemLongClicked(final FenceEntity fend1) {
                final String token = PreferencesUtils.getString(FenceListActivity.this, "Token");
                AlertDialog.Builder builder = new AlertDialog.Builder(FenceListActivity.this)
                        .setTitle("删除围栏")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deleteFence(token,fend1.getId()+"");



                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(mPresenter !=null){
                                    mPresenter.loadFendList();
                                }
                            }
                        });
                builder.show();

            }
        });
    }
    @Override
    public void showFendList(List<FenceEntity> fendList) {
        fenceAdapter.setImeiList(fendList);
    }

    @Override
    public Context context() {
        return this;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.getItem(0).setTitle(R.string.desc_add_fence);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
//                String map = PreferencesUtils.getString(FenceListActivity.this, "map");
//                if(map.equals("Google Map")) {
//                    startActivity(new Intent(FenceListActivity.this, GFendActivity.class));
//                }else if(map.equals("高德地图")){
                    startActivity(new Intent(FenceListActivity.this, NewFendActivity.class));
//                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
