package com.boatchina.imerit.app.view.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.boatchina.imerit.app.R;
import com.boatchina.imerit.data.fence.FenceEntity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GFendActivity extends NormalActivity implements OnMapReadyCallback {

    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    GoogleMap mMap;
    Circle circle;
    int x;
    int y;
    FenceEntity fend;
    LatLng maxLatLng;
    LatLng minLatLng;
    boolean hasOne =false;
    @Override
    protected String getToolbarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gfend);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

                seekBar.setMax(100);
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int r = progress;
                int maxX = x + r;
                int maxY = y + r;
                int minX = x-r;
                int minY = y-r;

                Projection projection = mMap.getProjection();

                maxLatLng = projection.fromScreenLocation(new Point(maxX, maxY));
                minLatLng = projection.fromScreenLocation(new Point(minX, minY));
                double  radius = SphericalUtil.computeDistanceBetween(maxLatLng,minLatLng)/2;
                circle.setRadius(radius);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        fend = (FenceEntity) getIntent().getSerializableExtra("fend");
        if(fend==null) {
            seekBar.setVisibility(View.VISIBLE);
            hasOne=false;
        }else {
            seekBar.setVisibility(View.GONE);
            hasOne = true;
        }

    }

    @Override
    protected void initializeDi() {
        
    }

    private void addOnMap(FenceEntity fend) {
        double latitude = (fend.getLat1() + fend.getLat2()) / 2;
        double longitude = (fend.getLng1() + fend.getLng2()) / 2;
    double  v = SphericalUtil.computeDistanceBetween(new  LatLng(fend.getLat1(), fend.getLng1()),new LatLng(fend.getLat2(), fend.getLng2()))/2;
        double r = v / Math.sqrt(2);
        Circle circle = mMap.addCircle(new CircleOptions().center(new LatLng(latitude, longitude))
                .radius(r));
        System.out.println("---" + circle.getRadius() + "," + circle.getCenter().latitude + "," + circle.getCenter().longitude);
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("xxxx").draggable(true));

    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(latitude, longitude), 15.5f, 0, 30)));


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap  =googleMap;
        if(fend==null) {
        }else {

            addOnMap(fend);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(hasOne) {
                    return;
                }

//                System.out.println("////"+latLng.latitude+","+latLng.longitude);
                CircleOptions radius = new CircleOptions().center(latLng).radius(0);

                circle = mMap.addCircle(radius);

                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
                seekBar.setEnabled(true);
                hasOne = true;
//                double distance = SphericalUtil.computeDistanceBetween(latLng, new LatLng(latLng.latitude,add(latLng.longitude,0.01)));
//                System.out.println("////"+latLng.latitude+","+add(latLng.longitude,0.01));
//                System.out.println("////"+distance);
                Projection projection = mMap.getProjection();
                Point point = projection.toScreenLocation(latLng);

                x = point.x;
                y = point.y;

            }
        });

    }


    @OnClick(R.id.btn_config)
    public void onClick() {
//        if(fend==null) {
//            T.showShort(getString(R.string.tip_add_fence));
//            return;
//        }
//        startActivityForResult(new Intent(GFendActivity.this,FendConfigActivity.class).putExtra("fend",fend),1);
        if(fend==null) {
            fend = new FenceEntity();
            fend.setLat1(minLatLng.latitude);
            fend.setLat2(maxLatLng.latitude);
            fend.setLng1(minLatLng.longitude);
            fend.setLng2(maxLatLng.longitude);
        }else {

        }

        startActivityForResult(new Intent(GFendActivity.this,FendConfigActivity.class).putExtra("fend",fend),1);
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
                    fend = new FenceEntity();
                    fend.setLat1(minLatLng.latitude);
                    fend.setLat2(maxLatLng.latitude);
                    fend.setLng1(minLatLng.longitude);
                    fend.setLng2(maxLatLng.longitude);
                    startActivityForResult(new Intent(GFendActivity.this,FendConfigActivity.class).putExtra("fend",fend),1);



                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            finish();
        }

    }


}
