package com.sctjsj.mayk.wowallethost.model.javabean;

/**
 * Created by lifuy on 2017/5/18.
 */

public class AdvertBean {
    int id,viewType;
    String iconurl;

    public AdvertBean(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    @Override
    public String toString() {
        return "AdvertBean{" +
                "id=" + id +
                ", viewType=" + viewType +
                ", iconurl='" + iconurl + '\'' +
                '}';
    }
}
