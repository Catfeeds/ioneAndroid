package com.boatchina.imerit.app.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.TabComponent;
import com.boatchina.imerit.app.features.history.HistoryActivity;
import com.boatchina.imerit.app.features.location.LocationActivity;
import com.boatchina.imerit.app.features.login.LoginActivity;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.fragment.BaseFragment;
import com.boatchina.imerit.app.view.fragment.FindFragment;
import com.boatchina.imerit.app.view.fragment.HomeFragment;
import com.boatchina.imerit.app.features.message.InfoFragment;
import com.boatchina.imerit.app.view.fragment.MineFragment;
import com.boatchina.imerit.app.view.fragment.MyBaseFragment;
import com.boatchina.imerit.app.view.talk.TalkActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;


public class TabActivity extends NormalActivity implements BaseFragment.OnFragmentInteractionListener, AMapLocationListener,MyBaseFragment.OnFragmentInteractionListener{

    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_info)
    RadioButton rbInfo;
    @BindView(R.id.rb_find)
    RadioButton rbFind;
    @BindView(R.id.rb_mine)
    RadioButton rbMine;
    @BindView(R.id.rg)
    RadioGroup rg;
//    @Inject
//    BriteDatabase briteDatabase;
    FragmentManager fm;
     HomeFragment homeFragment;
     FindFragment findFragment;
     InfoFragment infoFragment;
     MineFragment mineFragment;
    private static final  String PRV_SELINDEX="PREV_SELINDEX";
    private String selindex="HomeFragment";

    Menu menu;
    private AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(PRV_SELINDEX,selindex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tab);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        if(!NotificationManagerCompat.from(TabActivity.this).areNotificationsEnabled()) {
            T.showShort("请允许通知，否则通知栏无法显示通知");
        }
        String token = PreferencesUtils.getString(this, "Token");
        if(token!=null) System.out.println("token="+token);
//        CrashReport.testJavaCrash();

//        if (savedInstanceState != null) {
//            selindex=savedInstanceState.getString(PRV_SELINDEX,selindex);
//            homeFragment = (HomeFragment) fm.findFragmentByTag("HomeFragment");
//            findFragment = (FindFragment) fm.findFragmentByTag("FindFragment");
//            infoFragment = (InfoFragment) fm.findFragmentByTag("InfoFragment");
//            mineFragment = (MineFragment) fm.findFragmentByTag("MineFragment");
//            switch (selindex){
//                case "HomeFragment":
//                    rbHome.setChecked(true);
//                    fragmentTransaction.show(homeFragment).commit();
//                    break;
//                case "FindFragment":
//                    rbFind.setChecked(true);
//                    fragmentTransaction.show(findFragment).commit();
//                    break;
//                case "InfoFragment":
//                    rbInfo.setChecked(true);
//                    fragmentTransaction.show(infoFragment).commit();
//                    break;
//                case "MineFragment":
//                    rbMine.setChecked(true);
//                    fragmentTransaction.show(mineFragment).commit();
//                    break;
//            }
//        }else {
            homeFragment = HomeFragment.newInstance(null, null);
            findFragment = FindFragment.newInstance(null, null);
            infoFragment = InfoFragment.newInstance(null, null);
            mineFragment = MineFragment.newInstance(null, null);
            initTab();


        //声明mLocationOption对象
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return;
        }
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);


        mlocationClient.startLocation();

    }

    @Override
    protected void initializeDi() {
        TabComponent tabComponent = AndroidApplication.getBaseApplication().getApplicationComponent().tabComponent(getActivityModule());

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
            mlocationClient = null;
        }
    }

    private void initTab() {



                fm = getSupportFragmentManager();
         FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container, homeFragment).add(R.id.fragment_container,
                    findFragment).add(R.id.fragment_container, infoFragment).add(R.id.fragment_container, mineFragment);
            fragmentTransaction.hide(homeFragment).hide(findFragment).hide(infoFragment).hide(mineFragment);


        if(getIntent().getIntExtra("index",0)==1) {
            rbInfo.setChecked(true);
            fragmentTransaction.show(infoFragment).commit();
        }else {
            rbHome.setChecked(true);
            fragmentTransaction.show(homeFragment).commit();
        }


        rg.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();

                            fragmentTransaction.hide(homeFragment).hide(findFragment).hide(infoFragment).hide(mineFragment);

                        switch (checkedId) {
                            case R.id.rb_find:
                                if(menu!=null) {
                                    menu.getItem(1).setVisible(false);
                                }
                                fragmentTransaction.show(findFragment).commit();
                                break;
                            case R.id.rb_home:
                                if(menu!=null) {
                                    menu.getItem(1).setVisible(false);
                                }
//                                JPushInterface.resumePush(getApplicationContext());
                                fragmentTransaction.show(homeFragment).commit();
                                break;
                            case R.id.rb_info:
                                JPushInterface.init(getApplicationContext());
//                                JPushInterface.stopPush(getApplicationContext());
                                if(menu!=null) {
                                    menu.getItem(1).setVisible(true);
                                }
                                fragmentTransaction.show(infoFragment).commit();
                                break;
                            case R.id.rb_mine:
//                                JPushInterface.init(getApplicationContext());
                                if(menu!=null) {
                                    menu.getItem(1).setVisible(false);
                                }
                                fragmentTransaction.show(mineFragment).commit();
                                break;
                        }
                    }
                }
        );


    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                T.showShort(getString(R.string.tip_exit));
                touchTime = currentTime;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onFragmentInteraction(int id) {
        switch (id){

            case R.id.iv_addimei:
                startActivity(new Intent(this,InputImeiActivity.class));
                break;
            case R.id.ll_loc:

                startActivity(new Intent(this,LocationActivity.class));
                break;
            case R.id.ll_history:
                startActivity(new Intent(this,HistoryActivity.class));
                break;
            case R.id.ll_phone:
                startActivity(new Intent(this,PhoneActivity.class));

                break;
            case R.id.view_setings:
                String token = PreferencesUtils.getString(TabActivity.this, "Token");
                if(token==null||token.equals("")) {
                    startActivity(new Intent(this,LoginActivity.class));
                }else {
                    startActivity(new Intent(this,ImeiListActivity.class));
                }

                break;
//            case R.id.view_help:
//
//                break;
            case R.id.view_about:
                startActivity(new Intent(this,AboutusActivity.class));
                break;
            case R.id.btn_quit:

                new android.app.AlertDialog.Builder(TabActivity.this).setTitle("是否要退出登录")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PreferencesUtils.putString(TabActivity.this,"Token","");
                                menu.getItem(0).setVisible(true);
                                if(mineFragment!=null) {
                                    mineFragment.setQuitShow(false);
                                }
                            }
                        })
                        .show();
                break;
            case R.id.view_map_type:
                final AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);  //先得到构造器
                builder.setTitle(R.string.title_select_map);                                     //设置标题
                final String[] array = new String[]{"高德地图","Google Map"};
                String map = PreferencesUtils.getString(TabActivity.this, "map");
                int index=-1;
                if(map!=null) {
                    index = map.equals("高德地图")?0:1;
                }
                builder.setSingleChoiceItems(array, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PreferencesUtils.putString(TabActivity.this, "map", array[which]);



                    }
                });
                builder.create().show();
                break;
            case R.id.ll_monitor:
                startActivity(new Intent(this,MonitorActivity.class));
                break;
            case R.id.ll_fence:
                startActivity(new Intent(this,FenceListActivity.class));
                break;
            case R.id.ll_talkback:
                startActivity(new Intent(this,TalkActivity.class));
                break;

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_login, menu);
        String token = PreferencesUtils.getString(TabActivity.this, "Token");
        if (token == null || token.equals("")) {
            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(menu!=null&&mineFragment!=null) {
            String token = PreferencesUtils.getString(TabActivity.this, "Token");
            if (token == null || token.equals("")) {
                menu.getItem(0).setVisible(true);
                mineFragment.setQuitShow(false);
            } else {
                menu.getItem(0).setVisible(false);
                mineFragment.setQuitShow(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                String token = PreferencesUtils.getString(TabActivity.this, "Token");
                if(token==null||token.equals("")) {
                    Intent intent =new Intent(TabActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {

                }

                break;
            case R.id.action_delete:
                if(infoFragment!=null) {
                    infoFragment.deleteAll();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                //获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                PreferencesUtils.putFloat(TabActivity.this,"Latitude", (float) amapLocation.getLatitude());
                PreferencesUtils.putFloat(TabActivity.this,"Longitude", (float) amapLocation.getLongitude());
                System.out.println("/////"+amapLocation.getLatitude()+","+amapLocation.getLongitude());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);


        mlocationClient.startLocation();
    }

}
