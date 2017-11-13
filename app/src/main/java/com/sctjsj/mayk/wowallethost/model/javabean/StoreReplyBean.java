package com.sctjsj.mayk.wowallethost.model.javabean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/6/19.
 * 店家回复评论
 */

public class StoreReplyBean implements Serializable {
    private int id;//id
    private String content;//内容
    private String insertTime;//评论时间
    private UserBean reviewer;//评论者

    public StoreReplyBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserBean getReviewer() {
        return reviewer;
    }

    public void setReviewer(UserBean reviewer) {
        this.reviewer = reviewer;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "StoreReplyBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", reviewer=" + reviewer +
                '}';
    }
}
