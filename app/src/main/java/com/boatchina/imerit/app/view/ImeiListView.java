package com.boatchina.imerit.app.view;

import com.boatchina.imerit.data.device.DeviceEntity;

import java.util.List;

/**
 * Created by fflamingogo on 2016/8/11.
 */
public interface ImeiListView extends LoadDataView {

    void showImeiList(List<DeviceEntity> imeiList);

    void addFendNext();
}
