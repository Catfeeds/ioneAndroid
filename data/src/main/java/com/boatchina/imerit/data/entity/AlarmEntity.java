package com.boatchina.imerit.data.entity;

/**
 * Created by fflamingogo on 2016/7/28.
 */
public class AlarmEntity {
    String type;
    int time;
    String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
