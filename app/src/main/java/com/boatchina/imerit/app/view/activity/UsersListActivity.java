package com.boatchina.imerit.app.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.entity.UsersEntity;
import com.boatchina.imerit.data.net.HttpMethods;
import com.boatchina.imerit.data.net.HttpResultFunc;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.adapter.MasterAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UsersListActivity extends NormalActivity {

    @BindView(R.id.rcv)
    RecyclerView rcv;

//    @Inject
    MasterAdapter masterAdapter;
    @Override
    protected String getToolbarTitle() {
        return "设置管理员";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_users_list);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        rcv.setLayoutManager(new LinearLayoutManager(UsersListActivity.this));
        masterAdapter = new MasterAdapter(this);
        rcv.setAdapter(masterAdapter);
        masterAdapter.setOnItemClickListener(new MasterAdapter.OnItemClickListener() {
            @Override
            public void onImeiItemClicked(final UsersEntity usersEntity) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UsersListActivity.this);
                builder.setTitle("是否移交管理员身份");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String imei = getIntent().getStringExtra("imei");
                        String token = PreferencesUtils.getString(UsersListActivity.this, "Token");
                        HttpMethods.getInstance().getService().bindmaster(token, imei, usersEntity.getId())
                                .map(new HttpResultFunc<BlankEntity>())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ProgressSubscriber<BlankEntity>(UsersListActivity.this, new SubscriberOnNextListener<BlankEntity>() {


                                    @Override
                                    public void onNext(BlankEntity blankEntity) {
                                        T.showShort("已经设置为管理员");
                                        finish();
                                    }
                                }));
                    }
                }).show();

            }
        });
        String imei = getIntent().getStringExtra("imei");
        String token = PreferencesUtils.getString(UsersListActivity.this, "Token");
        HttpMethods.getInstance().getService().getbondedusers(token, imei)
                .map(new HttpResultFunc<List<UsersEntity>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<List<UsersEntity>>(this, new SubscriberOnNextListener<List<UsersEntity>>() {


                    @Override
                    public void onNext(List<UsersEntity> usersModels) {
                        masterAdapter.setImeiList(usersModels);
                    }
                }));


    }

    @Override
    protected void initializeDi() {

    }


}
