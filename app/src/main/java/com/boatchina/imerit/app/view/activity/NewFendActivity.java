package com.boatchina.imerit.app.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.LaglngEntity;
import com.boatchina.imerit.data.fence.FenceEntity;
import com.boatchina.imerit.data.net.HttpMethods;
import com.boatchina.imerit.data.net.HttpResultFunc;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NewFendActivity extends NormalActivity {

    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    Circle circle;
    Marker marker;
    FenceEntity fend;

//    boolean hasOne = false;
    @BindView(R.id.map)
    MapView map;
    private AMap aMap;

    @Override
    protected String getToolbarTitle() {
        return "添加围栏";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fend_new);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        map.onCreate(savedInstanceState);
        init();
        seekBar.setMax(1000);
        fend = (FenceEntity) getIntent().getSerializableExtra("fend");
        if (fend == null) {
            float latitude = PreferencesUtils.getFloat(NewFendActivity.this, "Latitude");
            float longitude = PreferencesUtils.getFloat(NewFendActivity.this, "Longitude");
           aMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(
                    BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("本机").draggable(false));
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    new LatLng(latitude, longitude), 15.5f, 0, 30)));
            seekBar.setEnabled(false);
            seekBar.setProgress(1000);
        } else {

            addOnMap(fend);
            seekBar.setProgress(Integer.parseInt(fend.getRadius()));

        }
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(marker!=null) {
                    marker.destroy();
                }

                aMap.clear();

                circle = aMap.addCircle(new CircleOptions().center(latLng).radius(1000)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(50, 1, 1, 1))
                        .strokeWidth(5));
                seekBar.setProgress(1000);
                aMap.invalidate();


                marker = aMap.addMarker(new MarkerOptions().position(latLng).title("中心").draggable(true));


                seekBar.setEnabled(true);






            }
        });
        aMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

                circle.setCenter(marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                circle.setRadius(progress);

                aMap.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    @Override
    protected void initializeDi() {

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();

    }

    private void init() {
        if (aMap == null) {
            aMap = map.getMap();
        }



    }
    private void addOnMap(FenceEntity fend) {

        circle = aMap.addCircle(new CircleOptions().center(new LatLng(fend.getLat1(), fend.getLng1()))
                .radius(Double.parseDouble(fend.getRadius()))
                .strokeColor(
                        Color.RED)
                .fillColor(Color.argb(50, 1, 1, 1))
                .strokeWidth(5));
        aMap.addMarker(new MarkerOptions().position(new LatLng(fend.getLat1(), fend.getLng1())).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(fend.getName()).draggable(true));

        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(fend.getLat1(), fend.getLng1()), 15.5f, 0, 30)));


    }



    @OnClick(R.id.btn_config)
    public void onClick() {
        if (fend == null) {
            if(circle==null) {
                T.showShort("请添加围栏");
                return;
            }
            fend = new FenceEntity();
            System.out.println("---"+circle.getCenter().latitude+"---"+circle.getCenter().longitude);
//           CoordinateConverter coordinateConverter = new CoordinateConverter();
//        LatLng latLng = coordinateConverter.from(CoordinateConverter.CoordType.MAPABC).coord(circle.getCenter()).convert();
//            fend.setLat1(latLng.latitude);
//            fend.setLng1(latLng.longitude);
//            System.out.println("---"+latLng.latitude+"---"+latLng.longitude);
            HttpMethods.getInstance().getService().convert("gcj02","wgs84",circle.getCenter().latitude,circle.getCenter().longitude)
                    .map(new HttpResultFunc())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new ProgressSubscriber<LaglngEntity>(NewFendActivity.this, new SubscriberOnNextListener<LaglngEntity>() {


                        @Override
                        public void onNext(LaglngEntity laglngEntity) {
                            System.out.println("---"+laglngEntity.getLat()+laglngEntity.getLng());
                            fend.setLat1(laglngEntity.getLat());
                            fend.setLng1(laglngEntity.getLng());
                            fend.setRadius(circle.getRadius()+"");
                            startActivityForResult(new Intent(NewFendActivity.this, FendConfigActivity.class).putExtra("fend", fend), 1);
                        }
                    }));

//            fend.setLat1(circle.getCenter().latitude);
//            fend.setLng1(circle.getCenter().longitude);



        } else {
            HttpMethods.getInstance().getService().convert("gcj02","wgs84",circle.getCenter().latitude,circle.getCenter().longitude)
                    .map(new HttpResultFunc())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new ProgressSubscriber<LaglngEntity>(NewFendActivity.this, new SubscriberOnNextListener<LaglngEntity>() {


                        @Override
                        public void onNext(LaglngEntity laglngEntity) {
                            System.out.println("---"+laglngEntity.getLat()+laglngEntity.getLng());
                            fend.setLat1(laglngEntity.getLat());
                            fend.setLng1(laglngEntity.getLng());
                            fend.setRadius(circle.getRadius()+"");
                            startActivityForResult(new Intent(NewFendActivity.this, FendConfigActivity.class).putExtra("fend", fend), 1);
                        }
                    }));

//            fend.setLat1(circle.getCenter().latitude);
//
//            fend.setLng1(circle.getCenter().longitude);
//            fend.setRadius(circle.getRadius()+"");
//            startActivityForResult(new Intent(NewFendActivity.this, FendConfigActivity.class).putExtra("fend", fend), 1);
        }


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            finish();
        }

    }


}
