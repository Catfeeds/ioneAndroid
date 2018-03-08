package com.boatchina.imerit.data.fence;
import com.boatchina.imerit.data.entity.BaseEntity;

import java.util.List;

/**
 * Created by fflamingogo on 2016/7/28.
 */
public class FenceEntity extends BaseEntity {
  private  int id;
  private  String name;
  private  String type;
  private  double lng1;
  private  double lat1;
  private  double lng2;
  private  double lat2;
  private List<String> devices;
    private String radius;

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLng1() {
        return lng1;
    }

    public void setLng1(double lng1) {
        this.lng1 = lng1;
    }

    public double getLat1() {
        return lat1;
    }

    public void setLat1(double lat1) {
        this.lat1 = lat1;
    }

    public double getLng2() {
        return lng2;
    }

    public void setLng2(double lng2) {
        this.lng2 = lng2;
    }

    public double getLat2() {
        return lat2;
    }

    public void setLat2(double lat2) {
        this.lat2 = lat2;
    }
}
