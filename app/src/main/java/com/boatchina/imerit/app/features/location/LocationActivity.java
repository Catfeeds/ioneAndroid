package com.boatchina.imerit.app.features.location;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.view.activity.BaseActivity;
import com.boatchina.imerit.app.view.activity.login.GaoDeMapStatus;
import com.boatchina.imerit.app.view.custom.ImeiSelectView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends BaseActivity<LocationView,LocationPresenter,LocationComponent> implements LocationView, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener {

    @BindView(R.id.map)
    MapView map;

    AMap aMap;

    @BindView(R.id.imeiselectview)
    ImeiSelectView imeiselectview;

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_location);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_location);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        map.onCreate(savedInstanceState);// 此方法必须重写
        aMap = map.getMap();
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
//        mPresenter.attachView(this);
        mPresenter.setStatus(new GaoDeMapStatus());
        mPresenter.showLocation();


        imeiselectview.setOnSelect(new ImeiSelectView.OnSelect() {
            @Override
            public void select(String item) {
                mPresenter.selectLocation(item);
            }
        });

    }

    @Override
    protected LocationComponent initializeDi() {
        LocationComponent locationComponent = AndroidApplication.getBaseApplication().getApplicationComponent().locationComponent();
        return locationComponent;
    }

    @Override
    protected void injectDependencies(LocationComponent component) {
        component.inject(this);
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
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }




    @Override
    public Context context() {
        return this;
    }




    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }



    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {

        aMap.moveCamera(update);
    }

    @Override
    public void addOnMap(double latitude, double longitude, String title) {
        LatLng latLng = new LatLng(latitude,longitude);
        Marker marker = aMap.addMarker(new MarkerOptions().title(title).draggable(true).position(latLng).icon(
                BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_location)));
        marker.showInfoWindow();
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        latLng, 15, 0, 30)), null);
    }

}
