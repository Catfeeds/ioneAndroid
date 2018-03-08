package com.boatchina.imerit.app.features.history;

import com.boatchina.imerit.app.view.LoadDataView;
import com.boatchina.imerit.data.location.LocationEntity;

import java.util.List;

/**
 * Created by fflamingogo on 2016/7/22.
 */
public interface HistoryView extends LoadDataView {

    void showDate(int year, int month, int date);

    void addOnMap(List<LocationEntity> locationModels);
}
