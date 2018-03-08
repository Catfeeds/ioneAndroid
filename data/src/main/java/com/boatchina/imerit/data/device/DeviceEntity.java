package com.boatchina.imerit.data.device;

import com.boatchina.imerit.data.entity.BaseEntity;

/**
 * Created by fflamingogo on 2016/7/25.
 */
public class DeviceEntity extends BaseEntity {
    String imei;
    String name;
    int interval;
    String phone;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
