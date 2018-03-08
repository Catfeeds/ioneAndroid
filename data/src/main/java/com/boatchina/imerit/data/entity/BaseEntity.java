package com.boatchina.imerit.data.entity;

import java.io.Serializable;

/**
 * Created by fflamingogo on 2016/7/22.
 */
public class BaseEntity<T> implements Serializable{
    private int errcode;
    private String errmsg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
