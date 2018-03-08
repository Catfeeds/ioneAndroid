package com.boatchina.imerit.data.location;

import com.boatchina.imerit.data.entity.BaseEntity;

/**
 * Created by fflamingogo on 2016/7/21.
 */
public class LocationEntity extends BaseEntity {
        private String lat;
        private String lng;
        private String speed;
        private String bearing;
        private String address;
        private int time;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getBearing() {
            return bearing;
        }

        public void setBearing(String bearing) {
            this.bearing = bearing;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

}
