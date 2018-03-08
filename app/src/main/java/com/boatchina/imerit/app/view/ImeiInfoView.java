package com.boatchina.imerit.app.view;

import com.boatchina.imerit.data.device.DeviceEntity;

/**
 * Created by fflamingogo on 2016/8/11.
 */
public interface ImeiInfoView extends LoadDataView {
    void unBindNext();

    void showData(DeviceEntity bond);

    void changeTime(String time);
}
