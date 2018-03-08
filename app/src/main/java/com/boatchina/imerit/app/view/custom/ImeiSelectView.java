package com.boatchina.imerit.app.view.custom;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.BondsComponent;
import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.view.activity.InputImeiActivity;
import com.boatchina.imerit.app.features.login.LoginActivity;
import com.boatchina.imerit.data.DefaultSubscriber;
import com.boatchina.imerit.data.device.DeviceEntity;
import com.boatchina.imerit.data.device.DeviceRepo;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.id.text1;
import static android.R.layout.simple_list_item_1;


/**
 * Created by fflamingogo on 2016/8/8.
 */
public class ImeiSelectView extends FrameLayout {
    @BindView(R.id.tv_imei_name)
    TextView tvImeiName;
    Context context;


    @BindView(R.id.iv_select_imei)
    ImageView ivSelectImei;
    OnSelect onSelect;
    @Inject
    DeviceRepo deviceRepo;
    @BindView(R.id.ll_imei)
    LinearLayout llImei;

    public ImeiSelectView(Context context) {
        super(context);
    }

    public ImeiSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_select_imei, this);
        BondsComponent bondsComponent = AndroidApplication.getBaseApplication().getApplicationComponent().bondsComponent(new ActivityModule((Activity) context));
        bondsComponent.inject(this);

        ButterKnife.bind(this);
        loadStatus();


    }

    private void loadStatus() {
        String token = PreferencesUtils.getString(context, "Token");
        if (token == null || token.equals("")) {
            tvImeiName.setText(R.string.desc_login_first);
            llImei.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            });
        } else {
            String imei = PreferencesUtils.getString(context, "imei");
            if (imei == null) {
                tvImeiName.setText(R.string.title_select_device);
            } else {
                tvImeiName.setText(imei);
            }

            deviceRepo.getBonds(token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new getBondsSubscriber());
        }
    }

    public ImeiSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnSelect {
        void select(String item);
    }

    public void setOnSelect(OnSelect onSelect) {
        this.onSelect = onSelect;
    }

    private void initDialog(final List<DeviceEntity> bonds) {

//        Observable.from(bonds).map(new Func1<DeviceEntity, String>() {
//            @Override
//            public String call(DeviceEntity bond) {
//                return bond.getImei();
//            }
//        }).toList().subscribe(new Action1<List<String>>() {
//            @Override
//            public void call(List<String> strings) {
//                final String[] array = strings.toArray(new String[0]);

        if (bonds.size() == 0) {
            context.startActivity(new Intent(context, InputImeiActivity.class));
            return;
        } else if (bonds.size() == 1) {
            if (bonds.get(0).getName().trim().equals("")) {
                tvImeiName.setText(bonds.get(0).getImei());
            } else {
                tvImeiName.setText(bonds.get(0).getName());
            }
            PreferencesUtils.putString(context, "imei", bonds.get(0).getImei());
        } else {
            String imei = PreferencesUtils.getString(context, "imei");
            if (imei == null) {

            } else {
                for (int i = 0; i < bonds.size(); i++) {
                    if (bonds.get(i).getImei().equals(imei)) {
                        tvImeiName.setText(bonds.get(i).getName());
                    }
                }
            }
        }
        //TODO
//                else if(array.length>1&&PreferencesUtils.getString(context,"imei")==null) {
//                    tvImeiName.setText(array[0]);
//                    PreferencesUtils.putString(context, "imei", array[0]);
//                }


        llImei.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 0;
                String imei = PreferencesUtils.getString(context, "imei");
                for (int i = 0; i < bonds.size(); i++) {
                    if (bonds.get(i).getImei().equals(imei)) {
                        index = i;
                    }
                }

                MyAdapter myAdapter = new MyAdapter(context, bonds);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);  //先得到构造器
                builder.setTitle(R.string.title_select_device);                                     //设置标题
                builder.setSingleChoiceItems(myAdapter, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (bonds.get(which).getName().trim().equals("")) {
                            tvImeiName.setText(bonds.get(which).getImei());
                        } else {
                            tvImeiName.setText(bonds.get(which).getName());
                        }
                        PreferencesUtils.putString(context, "imei", bonds.get(which).getImei());
                        if (onSelect != null) {
                            onSelect.select(bonds.get(which).getImei());
                        }
                    }
                });
                builder.create().show();
            }
        });

//                //  设置监听器
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });

//            }
//        });

    }

    public void setTheImei(String imei) {
        tvImeiName.setText(imei);
        PreferencesUtils.putString(context, "imei", imei);
        if (onSelect != null) {
            onSelect.select(imei);
        }
    }

    private final class getBondsSubscriber extends DefaultSubscriber<List<DeviceEntity>> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(List<DeviceEntity> response) {
            initDialog(response);
        }
    }

    public class MyAdapter extends BaseAdapter {
        List<DeviceEntity> list;
        LayoutInflater mInflater;

        public MyAdapter(Context context, List<DeviceEntity> list) {
            mInflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(simple_list_item_1, null);
            }

            TextView title = (TextView) convertView.findViewById(text1);
            title.setText(list.get(position).getName());

            return convertView;
        }
    }
}
