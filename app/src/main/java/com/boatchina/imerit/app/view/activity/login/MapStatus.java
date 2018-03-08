package com.boatchina.imerit.app.view.activity.login;

import com.boatchina.imerit.data.location.LocationEntity;

/**
 * Created by fflamingogo on 2016/10/10.
 */

public interface MapStatus {
    MyLatLng data2Map(LocationEntity location);
}
