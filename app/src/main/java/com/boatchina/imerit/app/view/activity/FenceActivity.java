package com.boatchina.imerit.app.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.FenceComponent;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.custom.MyView;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.fence.FenceEntity;
import com.boatchina.imerit.data.fence.FenceRepo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FenceActivity extends NormalActivity implements AMap.OnMarkerDragListener{
    Map<Integer, MyCircle> hash = new HashMap();
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.myview)
    MyView myview;
    private AMap aMap;

    @Inject
    FenceRepo fenceRepo;

    FenceEntity fend;
    Menu menu;
    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_fence);
    }
//    List<Fend> fends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fend);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        map.onCreate(savedInstanceState);


        init();
        aMap.setOnMarkerDragListener(this);

        aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Iterator<Integer> iterator = hash.keySet().iterator();
                while (iterator.hasNext()) {
                    final int next = iterator.next();
                    final Circle circle = hash.get(next).getCircle();
                    if (circle.contains(latLng)) {

                        String token = PreferencesUtils.getString(FenceActivity.this, "Token");
                        fenceRepo.delFine(token, next + "")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ProgressSubscriber<>(FenceActivity.this, new SubscriberOnNextListener<BlankEntity>() {
                            @Override
                            public void onNext(BlankEntity blank) {
                                circle.remove();
                                hash.get(next).getMarker().remove();

                            }
                        }));
                    }
                }
            }
        });




        fend = (FenceEntity) getIntent().getSerializableExtra("fend");
        if(fend==null) {
            float latitude = PreferencesUtils.getFloat(FenceActivity.this, "Latitude");
            float longitude = PreferencesUtils.getFloat(FenceActivity.this, "Longitude");
            Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(
                    BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("xxxx").draggable(true));
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    new LatLng(latitude, longitude), 15.5f, 0, 30)));
        }else {

            addOnMap(fend);
        }

    }

    @Override
    protected void initializeDi() {
        FenceComponent fenceComponent =  AndroidApplication.getBaseApplication().getApplicationComponent().fenceComponent(getActivityModule());
        fenceComponent.inject(this);
    }


    private void addOnMap(FenceEntity fend) {
        double latitude = (fend.getLat1() + fend.getLat2()) / 2;
        double longitude = (fend.getLng1() + fend.getLng2()) / 2;
        double v = AMapUtils.calculateLineDistance(new LatLng(fend.getLat1(), fend.getLng1()), new LatLng(fend.getLat2(), fend.getLng2())) / 2;
        double r = v / Math.sqrt(2);
        Circle circle = aMap.addCircle(new CircleOptions().center(new LatLng(latitude, longitude))
                .radius(r));
        System.out.println("---" + circle.getRadius() + "," + circle.getCenter().latitude + "," + circle.getCenter().longitude);
        Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("xxxx").draggable(true));

        Point p1 = aMap.getProjection().toScreenLocation(new LatLng(fend.getLat1(), fend.getLng1()));
        Point p2 = aMap.getProjection().toScreenLocation(new LatLng(fend.getLat2(), fend.getLng2()));
        int ox = (p1.x + p2.x) / 2;
        int radius = p2.x - ox;
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(latitude, longitude), 15.5f, 0, 30)));
        hash.put(fend.getId(), new MyCircle(circle, marker, radius));


    }

    private void init() {
        if (aMap == null) {
            aMap = map.getMap();
        }
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


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
//        LatLng position = marker.getPosition();
//        circle.setCenter(position);

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);
        if(fend!=null) {
            menu.getItem(0).setVisible(false);
        }
        menu.getItem(0).setTitle(R.string.desc_edit_mode);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:

                if (item.getTitle().equals(getString(R.string.desc_commit))) {
                    item.setTitle(R.string.desc_edit_mode);

                    MyView.Data data = myview.getData();
                    LatLng latLng = aMap.getProjection().fromScreenLocation(new Point((int) data.getMx(), (int) data.getMy()));
                    LatLng latLng2 = aMap.getProjection().fromScreenLocation(new Point((int) (data.getMx() + data.getSqrt()), (int) data.getMy()));
                    System.out.println("..." + data.getXx() + "," + data.getYy() + "," + data.getXX() + "," + data.getYY());
                    LatLng min = aMap.getProjection().fromScreenLocation(new Point((int) data.getXx(), (int) data.getYy()));
                    LatLng max = aMap.getProjection().fromScreenLocation(new Point((int) (data.getXX()), (int) data.getYY()));
                    System.out.println("..." + min.latitude + "," + min.longitude + "," + max.latitude + "," + max.longitude);
                    float v = AMapUtils.calculateLineDistance(latLng, latLng2);
                    aMap.addCircle(new CircleOptions().center(latLng)
                            .radius(v)
                            .fillColor(Color.argb(50, 1, 1, 1)));
//                    String token = PreferencesUtils.getString(this, "Token");
//                    String imei = PreferencesUtils.getString(this, "imei");
                    fend = new FenceEntity();
                    fend.setLat1(min.latitude);
                    fend.setLat2(max.latitude);
                    fend.setLng1(min.longitude);
                    fend.setLng2(max.longitude);
                    startActivityForResult(new Intent(FenceActivity.this,FendConfigActivity.class).putExtra("fend",fend),1);
//                    addFineUseCase.addFine(token, imei, "one", "circle", min.longitude + "", min.latitude + "", max.longitude + "", max.latitude + "", new ProgressSubscriber<Fend>(this, new SubscriberOnNextListener<Fend>() {
//                        @Override
//                        public void onNext(Fend finds) {
//                            myview.setVisibility(View.GONE);
//                        }
//
//
//                    }));
                } else {
                    item.setTitle(getString(R.string.desc_commit));
                    myview.setVisibility(View.VISIBLE);
                    if(fend!=null) {
                        myview.showOnView(aMap, fend);
                    }
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_config)
    public void onClick() {
        if(fend==null) {
            T.showShort(getString(R.string.tip_add_fence));
            return;
        }
        startActivityForResult(new Intent(FenceActivity.this,FendConfigActivity.class).putExtra("fend",fend),1);
    }


//    @Override
//    public void onLocationChanged(AMapLocation amapLocation) {
//
////        Marker marker = aMap.addMarker(new MarkerOptions().position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())).icon(
////                BitmapDescriptorFactory
////                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("xxxx").draggable(true));
//    }

    public class MyCircle {
        Circle circle;
        Marker marker;
        int r;


        public MyCircle(Circle circle, Marker marker, int r) {
            this.circle = circle;
            this.marker = marker;
            this.r = r;
        }

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }

        public Circle getCircle() {
            return circle;
        }

        public void setCircle(Circle circle) {
            this.circle = circle;
        }

        public Marker getMarker() {
            return marker;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            finish();
        }

    }
}
