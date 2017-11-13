package com.sctjsj.mayk.wowallethost.model.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuha on 2017/5/18.
 */

public class EvaluateBean {
    private int id;
    private boolean isboosEva=true;
    private List<String> commentPhoto;//评论上传的相片
    private int score;//评分
    private int isAnonymous;//是否匿名
    private String insertTime;
    private String content;//评论的内容
    private UserBean reviewer;//评论者
    private StoreReplyBean storeReplyBean;//店家回复评论

    public EvaluateBean(boolean isboosEva) {
        this.isboosEva = isboosEva;
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

    public StoreReplyBean getStoreReplyBean() {
        return storeReplyBean;
    }

    public void setStoreReplyBean(StoreReplyBean storeReplyBean) {
        this.storeReplyBean = storeReplyBean;
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

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getCommentPhoto() {
        return commentPhoto;
    }

    public void setCommentPhoto(List<String> commentPhoto) {
        this.commentPhoto = commentPhoto;
    }

    public EvaluateBean() {
    }

    public boolean isboosEva() {
        return isboosEva;
    }

    public void setIsboosEva(boolean isboosEva) {
        this.isboosEva = isboosEva;
    }

    @Override
    public String toString() {
        return "EvaluateBean{" +
                "id=" + id +
                ", isboosEva=" + isboosEva +
                ", commentPhoto=" + commentPhoto +
                ", score=" + score +
                ", isAnonymous=" + isAnonymous +
                ", insertTime='" + insertTime + '\'' +
                ", reviewer=" + reviewer +
                '}';
    }
}
