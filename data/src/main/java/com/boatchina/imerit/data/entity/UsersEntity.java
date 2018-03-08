package com.boatchina.imerit.data.entity;

/**
 * Created by Administrator on 2016/12/7.
 */

public class UsersEntity extends BaseEntity {
    private String id;
    private String name;

    public UsersEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
