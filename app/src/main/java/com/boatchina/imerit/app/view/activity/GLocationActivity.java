package com.boatchina.imerit.app.view.activity;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.features.location.LocationComponent;
import com.boatchina.imerit.app.features.location.LocationPresenter;
import com.boatchina.imerit.app.features.location.LocationView;
import com.boatchina.imerit.app.view.activity.login.GoogleMapStatus;
import com.boatchina.imerit.app.view.custom.ImeiSelectView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GLocationActivity extends NormalActivity implements OnMapReadyCallback, LocationView {


    @BindView(R.id.imeiselectview)
    ImeiSelectView imeiselectview;
    private GoogleMap mMap;
    Circle circle;
    @Inject
    LocationPresenter locationPresenter;

    @Override
    protected String getToolbarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationPresenter.setStatus(new GoogleMapStatus());
        locationPresenter.showLocation();
        imeiselectview.setOnSelect(new ImeiSelectView.OnSelect() {
            @Override
            public void select(String item) {
                locationPresenter.selectLocation(item);
            }
        });

//        seekBar.setMax(100);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                circle.setRadius(50000+progress*10000);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    @Override
    protected void initializeDi() {
        LocationComponent locationComponent = AndroidApplication.getBaseApplication().getApplicationComponent().locationComponent();
        locationComponent.inject(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                CircleOptions radius = new CircleOptions().center(latLng).radius(100000);

                circle = mMap.addCircle(radius);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));

            }
        });
    }



    @Override
    public Context context() {
        return this;
    }

    @Override
    public void addOnMap(double latitude, double longitude, String title) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(latitude, longitude), 15.5f, 0, 30)), null);
    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {

        mMap.moveCamera(update);
    }


}
