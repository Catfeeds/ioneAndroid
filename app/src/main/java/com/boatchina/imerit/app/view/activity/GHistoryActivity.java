package com.boatchina.imerit.app.view.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.features.history.HistoryComponent;
import com.boatchina.imerit.app.features.history.HistoryPresenter;
import com.boatchina.imerit.app.features.history.HistoryView;
import com.boatchina.imerit.app.view.custom.ImeiSelectView;
import com.boatchina.imerit.data.location.LocationEntity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GHistoryActivity extends NormalActivity implements OnMapReadyCallback, HistoryView {
    @Inject
    HistoryPresenter historyPresenter;
    GoogleMap googleMap;
    @BindView(R.id.imeiselectview)
    ImeiSelectView imeiselectview;
    @BindView(R.id.tv_time)
    TextView tvTime;

    @Override
    protected String getToolbarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghistory);
        ButterKnife.bind(this);
        historyPresenter.init();
        historyPresenter.showHistory();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"));


                int year = today.get(Calendar.YEAR);
                int month = today.get(Calendar.MONTH);
                int day = today.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(GHistoryActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        historyPresenter.selectDate(i,i1,i2);


                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });
        imeiselectview.setOnSelect(new ImeiSelectView.OnSelect() {
            @Override
            public void select(String item) {
                historyPresenter.selectDevice(item);
            }
        });
    }

    @Override
    protected void initializeDi() {
        HistoryComponent historyComponent =  AndroidApplication.getBaseApplication().getApplicationComponent().historyComponent(getActivityModule());
        historyComponent.inject(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    @Override
    public void showDate(int year, int month, int date) {
        tvTime.setText(year+"-"+month+"-"+date);
    }

    @Override
    public void addOnMap(List<LocationEntity> locationModels) {
        for (int i = 0; i < locationModels.size() - 1; i++) {
            LocationEntity locationModeA = locationModels.get(i);
            double lat = Double.parseDouble(locationModeA.getLat());
            double lng = Double.parseDouble(locationModeA.getLng());
            LatLng latLngA = new LatLng(lat, lng);
            LocationEntity locationModelB = locationModels.get(i + 1);
            double lat1 = Double.parseDouble(locationModelB.getLat());
            double lng1 = Double.parseDouble(locationModelB.getLng());
            LatLng latLngB = new LatLng(lat1, lng1);
            changeCamera(
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            latLngA, 15.5f, 0, 30)), null);
            googleMap.addPolyline((new PolylineOptions()).add(latLngA, latLngB).color(getResources().getColor(R.color.colorPrimary)));
        }
    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {

        googleMap.moveCamera(update);
    }

    @Override
    public Context context() {
        return this;
    }


}
