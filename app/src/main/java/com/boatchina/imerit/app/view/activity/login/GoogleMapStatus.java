package com.boatchina.imerit.app.view.activity.login;

import com.boatchina.imerit.app.utils.TimeUtils;
import com.boatchina.imerit.data.location.LocationEntity;

/**
 * Created by fflamingogo on 2016/10/10.
 */

public class GoogleMapStatus implements MapStatus {

    @Override
    public MyLatLng data2Map(LocationEntity location) {
        double lat = Double.parseDouble(location.getLat());
        double lng = Double.parseDouble(location.getLng());
        String title= "";
        try {
            title = location.getAddress() + "\n" + TimeUtils.timeFormat(location.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MyLatLng(lat,lng,title);
    }
}
