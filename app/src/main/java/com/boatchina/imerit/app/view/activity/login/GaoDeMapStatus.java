package com.boatchina.imerit.app.view.activity.login;

import com.boatchina.imerit.app.utils.TimeUtils;
import com.boatchina.imerit.data.location.LocationEntity;

/**
 * Created by fflamingogo on 2016/10/10.
 */

public class GaoDeMapStatus implements MapStatus {


    @Override
    public MyLatLng data2Map(LocationEntity location) {
        double lat = Double.parseDouble(location.getLat());
        double lng = Double.parseDouble(location.getLng());
//        CoordinateConverter coordinateConverter = new CoordinateConverter();
//        LatLng latLng = coordinateConverter.from(CoordinateConverter.CoordType.GPS).coord(new LatLng(lat, lng)).convert();
        String title= "";
        try {
            title = location.getAddress() + "\n"+TimeUtils.timeFormat(location.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MyLatLng(lat,lng,title);
    }
}
