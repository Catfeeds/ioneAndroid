package com.boatchina.imerit.data.entity;

/**
 * Created by fflamingogo on 2016/10/20.
 */

public class MyNews {
   private  int _id;
   private  String imei;
   private   int type;
   private  String fine;
   private  String status;
   private  String power;
   private boolean  isread;
   private long create_at;
    private  String user;
    private  String result;
    private  String voice;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MyNews(int _id, String imei, int type, String fine, String status, String power, boolean isread, long create_at, String user, String result, String voice, String msg) {
        this._id = _id;
        this.imei = imei;
        this.type = type;
        this.fine = fine;
        this.status = status;
        this.power = power;
        this.isread = isread;
        this.create_at = create_at;
        this.user = user;
        this.result = result;
        this.voice = voice;
        this.msg = msg;
    }

    public MyNews(int _id, String imei, int type, String fine, String status, String power, boolean isread, long create_at) {
        this._id = _id;
        this.imei = imei;
        this.type = type;
        this.fine = fine;
        this.status = status;
        this.power = power;
        this.isread = isread;
        this.create_at = create_at;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }



    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public boolean isread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }
}
