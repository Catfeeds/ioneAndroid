package com.boatchina.imerit.app.view.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.TimeConfigEntity;
import com.boatchina.imerit.data.net.HttpMethods;
import com.boatchina.imerit.data.net.HttpResultFunc;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.adapter.DisturbAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func7;
import rx.schedulers.Schedulers;

public class DisturbActivity extends NormalActivity {

    @BindView(R.id.rv_fend)
    RecyclerView rvFend;
    DisturbAdapter disturbAdapter;
    String imei;
    String token;
    String mRepeat;
    TextView startTime;
    TextView endTime;
    @Override
    protected String getToolbarTitle() {
        return "设置免打扰";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fend_list);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        imei = getIntent().getStringExtra("imei");
        token = PreferencesUtils.getString(DisturbActivity.this, "Token");
        rvFend.setLayoutManager(new LinearLayoutManager(DisturbActivity.this));
        disturbAdapter = new DisturbAdapter(DisturbActivity.this);
        disturbAdapter.setOnItemClickListener(new DisturbAdapter.OnItemClickListener() {
            @Override
            public void onImeiItemClicked(final TimeConfigEntity contactEntity) {
                View view = View.inflate(DisturbActivity.this, R.layout.view_disturb, null);

                startTime = (TextView) view.findViewById(R.id.tv_starttime);
                endTime = (TextView) view.findViewById(R.id.tv_endtime);
                final CheckBox cb7 = (CheckBox) view.findViewById(R.id.cb_7);
                final CheckBox cb1 = (CheckBox) view.findViewById(R.id.cb_1);
                final CheckBox cb2 = (CheckBox) view.findViewById(R.id.cb_2);
                final CheckBox cb3 = (CheckBox) view.findViewById(R.id.cb_3);
                final CheckBox cb4 = (CheckBox) view.findViewById(R.id.cb_4);
                final CheckBox cb5 = (CheckBox) view.findViewById(R.id.cb_5);
                final CheckBox cb6 = (CheckBox) view.findViewById(R.id.cb_6);

                startTime.setText(contactEntity.getBegin());
                endTime.setText(contactEntity.getEnd());
                String repeat = contactEntity.getRepeat();
                if(TextUtils.isEmpty(repeat)) {
                    return;
                }
                if(repeat.substring(0,1).equals("1")) {
                    cb7.setChecked(true);
                }else {
                    cb7.setChecked(false);
                }
                if(repeat.substring(1,2).equals("1")) {
                    cb1.setChecked(true);
                }else {
                    cb1.setChecked(false);
                }
                if(repeat.substring(2,3).equals("1")) {
                    cb2.setChecked(true);
                }else {
                    cb2.setChecked(false);
                }
                if(repeat.substring(3,4).equals("1")) {
                    cb3.setChecked(true);
                }else {
                    cb3.setChecked(false);
                }
                if(repeat.substring(4,5).equals("1")) {
                    cb4.setChecked(true);
                }else {
                    cb4.setChecked(false);
                }
                if(repeat.substring(5,6).equals("1")) {
                    cb5.setChecked(true);
                }else {
                    cb5.setChecked(false);
                }
                if(repeat.substring(6,7).equals("1")) {
                    cb6.setChecked(true);
                }else {
                    cb6.setChecked(false);
                }
                Observable<Boolean> ob7 = RxCompoundButton.checkedChanges(cb7);
                Observable<Boolean> ob6 = RxCompoundButton.checkedChanges(cb6);
                Observable<Boolean> ob5 = RxCompoundButton.checkedChanges(cb5);
                Observable<Boolean> ob4 = RxCompoundButton.checkedChanges(cb4);
                Observable<Boolean> ob3 = RxCompoundButton.checkedChanges(cb3);
                Observable<Boolean> ob2 = RxCompoundButton.checkedChanges(cb2);
                Observable<Boolean> ob1 = RxCompoundButton.checkedChanges(cb1);
                Observable.combineLatest(ob7, ob1, ob2, ob3, ob4, ob5, ob6, new Func7<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, String>() {
                    @Override
                    public String call(Boolean aBoolean, Boolean aBoolean2, Boolean aBoolean3, Boolean aBoolean4, Boolean aBoolean5, Boolean aBoolean6, Boolean aBoolean7) {
                        return ""+aBoolean.compareTo(false)+aBoolean2.compareTo(false)+aBoolean3.compareTo(false)+aBoolean4.compareTo(false)+aBoolean5.compareTo(false)+aBoolean6.compareTo(false)+aBoolean7.compareTo(false);
                    }
                }).subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        mRepeat = s;
                    }
                });

                startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog dialog  = new TimePickerDialog(DisturbActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                                String dateString = formatter.format(calendar.getTime());
                                startTime.setText(dateString);
                            }
                        },0,0,true);
                        dialog.show();
                    }
                });
                endTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog dialog  = new TimePickerDialog(DisturbActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                                String dateString = formatter.format(calendar.getTime());
                                endTime.setText(dateString);
                            }
                        },0,0,true);
                        dialog.show();
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(DisturbActivity.this)
                        .setView(view)
                        .setTitle("修改免打扰")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                                try {
                                    Date start = formatter.parse(startTime.getText().toString().trim());
                                    Date end = formatter.parse(endTime.getText().toString().trim());
                                    if(end.before(start)) {
                                        T.showShort("时间设置有误");
                                        return;
                                    }
                                    HttpMethods.getInstance().getService().setdevicend(token, imei,""+contactEntity.getIndex(),startTime.getText().toString().trim(),endTime.getText().toString().trim(),mRepeat)
                                            .map(new HttpResultFunc<List<Integer>>())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new ProgressSubscriber<List<Integer>>(DisturbActivity.this, new SubscriberOnNextListener<List<Integer>>() {


                                                @Override
                                                public void onNext(List<Integer> blankEntity) {

                                                }
                                            }));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                HttpMethods.getInstance().getService().getdevicend(token, imei)
                                        .map(new HttpResultFunc<List<TimeConfigEntity>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new ProgressSubscriber<List<TimeConfigEntity>>(DisturbActivity.this, new SubscriberOnNextListener<List<TimeConfigEntity>>() {


                                            @Override
                                            public void onNext(List<TimeConfigEntity> blankEntity) {
                                                disturbAdapter.setContantsList(blankEntity);
                                            }
                                        }));
                            }
                        });
                builder.show();
            }

            @Override
            public void onImeiItemLongClicked(final TimeConfigEntity timeConfigEntity) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisturbActivity.this)
                        .setTitle("删除免打扰")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    HttpMethods.getInstance().getService().setdevicend(token, imei,""+timeConfigEntity.getIndex(),"00:00","00:00","")
                                            .map(new HttpResultFunc<List<Integer>>())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new ProgressSubscriber<List<Integer>>(DisturbActivity.this, new SubscriberOnNextListener<List<Integer>>() {


                                                @Override
                                                public void onNext(List<Integer> blankEntity) {
                                                    T.showShort("删除成功");

                                                }
                                            }));


                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                HttpMethods.getInstance().getService().getdevicend(token, imei)
                                        .map(new HttpResultFunc<List<TimeConfigEntity>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new ProgressSubscriber<List<TimeConfigEntity>>(DisturbActivity.this, new SubscriberOnNextListener<List<TimeConfigEntity>>() {


                                            @Override
                                            public void onNext(List<TimeConfigEntity> blankEntity) {
                                                disturbAdapter.setContantsList(blankEntity);
                                            }
                                        }));
                            }
                        });
                builder.show();
            }
        });
        rvFend.setAdapter(disturbAdapter);
        HttpMethods.getInstance().getService().getdevicend(token, imei)
                .map(new HttpResultFunc<List<TimeConfigEntity>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber<List<TimeConfigEntity>>(DisturbActivity.this, new SubscriberOnNextListener<List<TimeConfigEntity>>() {


                    @Override
                    public void onNext(List<TimeConfigEntity> blankEntity) {
                        disturbAdapter.setContantsList(blankEntity);
                    }
                }));


    }

    @Override
    protected void initializeDi() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        menu.getItem(0).setTitle("添加时间");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                View view = View.inflate(DisturbActivity.this, R.layout.view_disturb, null);

                startTime = (TextView) view.findViewById(R.id.tv_starttime);
                endTime = (TextView) view.findViewById(R.id.tv_endtime);
                final CheckBox cb7 = (CheckBox) view.findViewById(R.id.cb_7);
                final CheckBox cb1 = (CheckBox) view.findViewById(R.id.cb_1);
                final CheckBox cb2 = (CheckBox) view.findViewById(R.id.cb_2);
                final CheckBox cb3 = (CheckBox) view.findViewById(R.id.cb_3);
                final CheckBox cb4 = (CheckBox) view.findViewById(R.id.cb_4);
                final CheckBox cb5 = (CheckBox) view.findViewById(R.id.cb_5);
                final CheckBox cb6 = (CheckBox) view.findViewById(R.id.cb_6);

                Observable<Boolean> ob7 = RxCompoundButton.checkedChanges(cb7);
                Observable<Boolean> ob6 = RxCompoundButton.checkedChanges(cb6);
                Observable<Boolean> ob5 = RxCompoundButton.checkedChanges(cb5);
                Observable<Boolean> ob4 = RxCompoundButton.checkedChanges(cb4);
                Observable<Boolean> ob3 = RxCompoundButton.checkedChanges(cb3);
                Observable<Boolean> ob2 = RxCompoundButton.checkedChanges(cb2);
                Observable<Boolean> ob1 = RxCompoundButton.checkedChanges(cb1);
                Observable.combineLatest(ob7, ob1, ob2, ob3, ob4, ob5, ob6, new Func7<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, String>() {
                    @Override
                    public String call(Boolean aBoolean, Boolean aBoolean2, Boolean aBoolean3, Boolean aBoolean4, Boolean aBoolean5, Boolean aBoolean6, Boolean aBoolean7) {
                        return ""+aBoolean.compareTo(false)+aBoolean2.compareTo(false)+aBoolean3.compareTo(false)+aBoolean4.compareTo(false)+aBoolean5.compareTo(false)+aBoolean6.compareTo(false)+aBoolean7.compareTo(false);
                    }
                }).subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        mRepeat = s;
                    }
                });
                startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog dialog  = new TimePickerDialog(DisturbActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                                String dateString = formatter.format(calendar.getTime());
                                startTime.setText(dateString);
                            }
                        },0,0,true);
                        dialog.show();
                    }
                });
                endTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog dialog  = new TimePickerDialog(DisturbActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                                String dateString = formatter.format(calendar.getTime());
                                endTime.setText(dateString);
                            }
                        },0,0,true);
                        dialog.show();
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(DisturbActivity.this)
                        .setView(view)
                        .setTitle("添加免打扰")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpMethods.getInstance().getService().setdevicend(token, imei,"",startTime.getText().toString().trim(),endTime.getText().toString().trim(),mRepeat)
                                        .map(new HttpResultFunc<List<Integer>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new ProgressSubscriber<List<Integer>>(DisturbActivity.this, new SubscriberOnNextListener<List<Integer>>() {


                                            @Override
                                            public void onNext(List<Integer> blankEntity) {
                                                HttpMethods.getInstance().getService().getdevicend(token, imei)
                                                        .map(new HttpResultFunc<List<TimeConfigEntity>>())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribeOn(Schedulers.io())
                                                        .subscribe(new ProgressSubscriber<List<TimeConfigEntity>>(DisturbActivity.this, new SubscriberOnNextListener<List<TimeConfigEntity>>() {


                                                            @Override
                                                            public void onNext(List<TimeConfigEntity> blankEntity) {
                                                                disturbAdapter.setContantsList(blankEntity);
                                                            }
                                                        }));
                                            }
                                        }));
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                HttpMethods.getInstance().getService().getdevicend(token, imei)
                                        .map(new HttpResultFunc<List<TimeConfigEntity>>())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new ProgressSubscriber<List<TimeConfigEntity>>(DisturbActivity.this, new SubscriberOnNextListener<List<TimeConfigEntity>>() {


                                            @Override
                                            public void onNext(List<TimeConfigEntity> blankEntity) {
                                                disturbAdapter.setContantsList(blankEntity);
                                            }
                                        }));
                            }
                        });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
