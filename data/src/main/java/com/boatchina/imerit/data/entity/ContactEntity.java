package com.boatchina.imerit.data.entity;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ContactEntity {
   private int index;
   private String number;
   private String name;

    public ContactEntity(int index, String number, String name) {
        this.index = index;
        this.number = number;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
