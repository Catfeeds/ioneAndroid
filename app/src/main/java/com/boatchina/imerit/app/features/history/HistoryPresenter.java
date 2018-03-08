package com.boatchina.imerit.app.features.history;

import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.location.LocationEntity;
import com.boatchina.imerit.data.location.LocationRepo;
import com.example.base.YaRxPresenter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fflamingogo on 2016/7/22.
 */
@PerActivity
public class HistoryPresenter extends YaRxPresenter<HistoryView> {


    private LocationRepo locationRepo;
  private long mStartTime;
  private long mEndTime;
  private String mToken;
  private String mImei;
    @Inject
    public HistoryPresenter(LocationRepo locationRepo) {
        this.locationRepo =locationRepo;
    }


    public void showHistory() {
        mImei = PreferencesUtils.getString(getView().context(), "imei");
        mImei = mImei==null?"":mImei;
        GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"));


        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);
        GregorianCalendar startTime = new GregorianCalendar(year, month, day, 0,0,0);
        startTime.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        GregorianCalendar endTime = new GregorianCalendar(year, month, day, 24,0,0);
        endTime.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mStartTime = startTime.getTimeInMillis()/1000;
        mEndTime = endTime.getTimeInMillis()/1000;
        getView().showDate(year,month+1,day);
        System.out.println("---"+mStartTime+"-"+""+mEndTime);
        loadHistory();
    }




    public void init() {
        mToken = PreferencesUtils.getString(getView().context(), "Token");
        mToken = mToken==null?"":mToken;
    }

    public void selectDevice(String imei) {
        this.mImei = imei;
        loadHistory();

    }


    private SubscriberOnNextListener subscriberOnNextListener = new SubscriberOnNextListener<List<LocationEntity>>() {

        @Override
        public void onNext(List<LocationEntity> locations) {
            if(locations.size()==0) {
                T.showShort(getView().context().getString(R.string.desc_no_history));
                return;
            }
            getView().addOnMap(locations);

//
        }

    };

    public void selectDate(int year, int month, int day) {
        GregorianCalendar startTime = new GregorianCalendar(year, month, day, 0,0,0);
        startTime.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mStartTime = startTime.getTimeInMillis() / 1000;
        GregorianCalendar endTime = new GregorianCalendar(year, month, day, 24,0,0);
        endTime.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mEndTime = endTime.getTimeInMillis() / 1000;
        loadHistory();
        System.out.println("---"+mStartTime+"-"+""+mEndTime);
        getView().showDate(year,month+1,day);
    }

    private void loadHistory() {
        addUtilStop(locationRepo.history(mToken,mImei,mStartTime,mEndTime,"gcj02")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber(getView().context(),subscriberOnNextListener))
        );
    }
}
