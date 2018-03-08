package com.boatchina.imerit.app.view.activity.login;

/**
 * Created by fflamingogo on 2016/10/10.
 */

public class MyLatLng {
    double lat;
    double lng;
    String title;

    public MyLatLng(double lat, double lng, String title) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTitle() {
        return title;
    }
}
