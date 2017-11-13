package com.sctjsj.mayk.wowallethost.model.javabean;

/**
 * Created by mayikang on 17/5/17.
 */

//图片实体
public class ImageBean {
    private int id;
    private String type;

    private String url;

    private boolean isUpdate;
    private int goodsGallery;

    private String tag;

    private int currentP=0;

    public int getGoodsGallery() {
        return goodsGallery;
    }

    public void setGoodsGallery(int goodsGallery) {
        this.goodsGallery = goodsGallery;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCurrentP() {
        return currentP;
    }

    public void setCurrentP(int currentP) {
        this.currentP = currentP;
    }

    public ImageBean(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
