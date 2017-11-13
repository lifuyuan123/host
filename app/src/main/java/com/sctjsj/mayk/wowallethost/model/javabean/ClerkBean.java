package com.sctjsj.mayk.wowallethost.model.javabean;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/6/20.
 */

public class ClerkBean implements Serializable {
    private int id;
    private String name;
    private int isdelete;

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

    public int getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(int isdelete) {
        this.isdelete = isdelete;
    }

    @Override
    public String toString() {
        return "ClerkBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isdelete=" + isdelete +
                '}';
    }
}
