package com.boatchina.imerit.app.features.history;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.PolylineOptions;
import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.view.activity.BaseActivity;
import com.boatchina.imerit.app.view.custom.ImeiSelectView;
import com.boatchina.imerit.data.location.LocationEntity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class HistoryActivity extends BaseActivity<HistoryView,HistoryPresenter,HistoryComponent> implements HistoryView {
    @BindView(R.id.map)
    MapView map;


    @BindView(R.id.imeiselectview)
    ImeiSelectView imeiselectview;
    @BindView(R.id.tv_time)
    TextView tvTime;
    AMap aMap;
    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_history);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mPresenter.init();
        mPresenter.showHistory();
        map.onCreate(savedInstanceState);// 此方法必须重写

        aMap = map.getMap();
        imeiselectview.setOnSelect(new ImeiSelectView.OnSelect() {
            @Override
            public void select(String item) {
                mPresenter.selectDevice(item);
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"));


                int year = today.get(Calendar.YEAR);
                int month = today.get(Calendar.MONTH);
                int day = today.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(HistoryActivity.this,new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            mPresenter.selectDate(i,i1,i2);


                        }
                    },year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();

//                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                        historyPresenter.selectDate(i,i1,i2);
//
//
//                    }
//                });
            }
        });

    }

    @Override
    protected HistoryComponent initializeDi() {
        HistoryComponent historyComponent = AndroidApplication.getBaseApplication().getApplicationComponent().historyComponent(getActivityModule());
        return historyComponent;
    }

    @Override
    protected void injectDependencies(HistoryComponent component) {
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
    public void showDate(int year, int month, int date) {
        tvTime.setText(year+"-"+month+"-"+date);
    }

    @Override
    public void addOnMap(List<LocationEntity> locationModels) {
//        CoordinateConverter coordinateConverter = new CoordinateConverter();
//        CoordinateConverter from = coordinateConverter.from(CoordinateConverter.CoordType.GPS);
        Observable.from(locationModels).map(new Func1<LocationEntity, LatLng>() {
            @Override
            public LatLng call(LocationEntity locationModel) {
                double lat = Double.parseDouble(locationModel.getLat());
                double lng = Double.parseDouble(locationModel.getLng());
                return new LatLng(lat,lng);
            }
        }).toList().subscribe(new Subscriber<List<LatLng>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<LatLng> latLngs) {
                aMap.addPolyline(new PolylineOptions().addAll(latLngs).color(getResources().getColor(R.color.colorPrimary)));
                changeCamera(
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            latLngs.get(latLngs.size()-1), 15.5f, 0, 30)), null);
            }
        });
//        for (int i = 0; i < locationModels.size()-500; i++) {
//            LocationModel locationModeA = locationModels.get(i);
//            double lat = Double.parseDouble(locationModeA.getLat());
//            double lng = Double.parseDouble(locationModeA.getLng());
//            LatLng latLngA = from.coord(new LatLng(lat, lng)).convert();
//
//            LocationModel locationModelB = locationModels.get(i+1);
//            double lat1 = Double.parseDouble(locationModelB.getLat());
//            double lng1 = Double.parseDouble(locationModelB.getLng());
//            LatLng latLngB = from.coord(new LatLng(lat1, lng1)).convert();
//            changeCamera(
//                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                            latLngA, 18, 0, 30)), null);
//            aMap.addPolyline((new PolylineOptions()).add(latLngA, latLngB).color(getResources().getColor(R.color.colorPrimary)));
//
//        }

    }
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {

        aMap.moveCamera(update);
    }

    @Override
    public Context context() {
        return this;
    }


}
