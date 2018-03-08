package com.boatchina.imerit.app.features.location;

import com.boatchina.imerit.app.view.LoadDataView;

/**
 * Created by fflamingogo on 2016/8/1.
 */
public interface LocationView extends LoadDataView {


    void addOnMap(double latitude, double longitude, String title);
}
