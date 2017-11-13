package com.sctjsj.mayk.wowallethost.model.javabean;

/**
 * Created by mayikang on 17/6/20.
 */

public class UploadImgBean {

    private int id;

    private String filePath;//需要上传的文件路径

    private int currentP = 0;//当前上传进度

    public UploadImgBean() {
    }

    public UploadImgBean(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public int getCurrentP() {
        return currentP;
    }

    public void setCurrentP(int currentP) {
        this.currentP = currentP;
    }
}
