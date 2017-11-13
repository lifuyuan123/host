package com.sctjsj.mayk.wowallethost.model.javabean;

import java.util.List;

/**
 * Created by mayikang on 17/5/16.
 */

//项目图片实体
public class ProjectImageData {

    //用于区分是图片还是添加图片的 item
    private int viewType;

    //用于区分是否显示删除角标
    private boolean showDelete;

    private ImageBean imageBean;

    public ProjectImageData(int viewType) {
        this.viewType = viewType;
    }



    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isShowDelete() {
        return showDelete;
    }

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
    }

    public ImageBean getImageBean() {
        return imageBean;
    }

    public void setImageBean(ImageBean imageBean) {
        this.imageBean = imageBean;
    }
}
