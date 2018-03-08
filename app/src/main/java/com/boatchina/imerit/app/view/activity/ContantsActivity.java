package com.boatchina.imerit.app.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.ContactEntity;
import com.boatchina.imerit.data.net.HttpMethods;
import com.boatchina.imerit.data.net.HttpResultFunc;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.adapter.ContantsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContantsActivity extends NormalActivity {

    @BindView(R.id.rv_fend)
    RecyclerView rvFend;
    ContantsAdapter contantsAdapter;
    String imei;
    String token;
    @Override
    protected String getToolbarTitle() {
        return "通讯录";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fend_list);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        imei = getIntent().getStringExtra("imei");
        token = PreferencesUtils.getString(ContantsActivity.this, "Token");
        rvFend.setLayoutManager(new LinearLayoutManager(ContantsActivity.this));
        contantsAdapter  = new ContantsAdapter(ContantsActivity.this);
        rvFend.setAdapter(contantsAdapter);
        contantsAdapter.setOnItemClickListener(new ContantsAdapter.OnItemClickListener() {
            @Override
            public void onImeiItemClicked(final ContactEntity contactEntity) {
                View view = View.inflate(ContantsActivity.this, R.layout.view_input_contants, null);
                final EditText editName = (EditText) view.findViewById(R.id.edit_name);
                editName.setText(contactEntity.getName());
                final EditText editPhone = (EditText) view.findViewById(R.id.edit_phone);
                editPhone.setText(contactEntity.getNumber());
                AlertDialog.Builder builder = new AlertDialog.Builder(ContantsActivity.this)
                        .setView(view)
                        .setTitle("修改通讯录")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpMethods.getInstance().getService().setdevicepb(token, imei,contactEntity.getIndex()+"",editPhone.getText().toString().trim(),editName.getText().toString().trim())
                                        .map(new HttpResultFunc<List<Integer>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new ProgressSubscriber<List<Integer>>(ContantsActivity.this, new SubscriberOnNextListener<List<Integer>>() {


                                            @Override
                                            public void onNext(List<Integer> blankEntity) {

                                            }
                                        }));
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                HttpMethods.getInstance().getService().getdevicepb(token, imei)
                                        .map(new HttpResultFunc<List<ContactEntity>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new ProgressSubscriber<List<ContactEntity>>(ContantsActivity.this, new SubscriberOnNextListener<List<ContactEntity>>() {

                                            @Override
                                            public void onNext(List<ContactEntity> contactEntities) {
                                                contantsAdapter.setContantsList(contactEntities);
                                            }
                                        }));
                            }
                        });
                builder.show();
            }

            @Override
            public void onImeiItemLongClicked(final ContactEntity contactEntity) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContantsActivity.this)

                        .setTitle("删除通讯录")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpMethods.getInstance().getService().setdevicepb(token, imei,contactEntity.getIndex()+"","","")
                                        .map(new HttpResultFunc<List<Integer>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new ProgressSubscriber<List<Integer>>(ContantsActivity.this, new SubscriberOnNextListener<List<Integer>>() {


                                            @Override
                                            public void onNext(List<Integer> blankEntity) {
                                                T.showShort("删除成功");

                                            }
                                        }));
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                HttpMethods.getInstance().getService().getdevicepb(token, imei)
                                        .map(new HttpResultFunc<List<ContactEntity>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new ProgressSubscriber<List<ContactEntity>>(ContantsActivity.this, new SubscriberOnNextListener<List<ContactEntity>>() {

                                            @Override
                                            public void onNext(List<ContactEntity> contactEntities) {
                                                contantsAdapter.setContantsList(contactEntities);
                                            }
                                        }));
                            }
                        });
                builder.show();
            }
        });
        HttpMethods.getInstance().getService().getdevicepb(token, imei)
                .map(new HttpResultFunc<List<ContactEntity>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber<List<ContactEntity>>(ContantsActivity.this, new SubscriberOnNextListener<List<ContactEntity>>() {

                    @Override
                    public void onNext(List<ContactEntity> contactEntities) {
                        contantsAdapter.setContantsList(contactEntities);
                    }
                }));

    }

    @Override
    protected void initializeDi() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.getItem(0).setTitle("添加通讯录");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                showDialog();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        View view = View.inflate(ContantsActivity.this, R.layout.view_input_contants, null);
        final EditText editName = (EditText) view.findViewById(R.id.edit_name);
        final EditText editPhone = (EditText) view.findViewById(R.id.edit_phone);
        AlertDialog.Builder builder = new AlertDialog.Builder(ContantsActivity.this)
                .setView(view)
                .setTitle("添加通讯录")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HttpMethods.getInstance().getService().setdevicepb(token, imei,"",editPhone.getText().toString().trim(),editName.getText().toString().trim())
                                .map(new HttpResultFunc<List<Integer>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new ProgressSubscriber<List<Integer>>(ContantsActivity.this, new SubscriberOnNextListener<List<Integer>>() {


                                    @Override
                                    public void onNext(List<Integer> blankEntity) {

                                    }
                                }));
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        HttpMethods.getInstance().getService().getdevicepb(token, imei)
                                .map(new HttpResultFunc<List<ContactEntity>>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new ProgressSubscriber<List<ContactEntity>>(ContantsActivity.this, new SubscriberOnNextListener<List<ContactEntity>>() {

                                    @Override
                                    public void onNext(List<ContactEntity> contactEntities) {
                                        contantsAdapter.setContantsList(contactEntities);
                                    }
                                }));
                    }
                });
        builder.show();
    }

}
