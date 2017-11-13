package com.sctjsj.mayk.wowallethost.model.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayikang on 17/5/12.
 */

public class CommentBean implements Serializable{
    private int id;
    //评论者 logo
    private String commentUserLogo;
    //是否匿名
    private int isAnonymous;

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    //评论者
    private String commentUserName;
    //评论时间
    private String commentTime;
    //评分
    private double score;
    //评论内容
    private String content;
    //评论的图片列表
    private List<String> photoList;
    //商家回复
    private String revert;
    //回复时间
    private String revertTime;

    public CommentBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentUserLogo() {
        return commentUserLogo;
    }

    public void setCommentUserLogo(String commentUserLogo) {
        this.commentUserLogo = commentUserLogo;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    public String getRevert() {
        return revert;
    }

    public void setRevert(String revert) {
        this.revert = revert;
    }

    public String getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(String revertTime) {
        this.revertTime = revertTime;
    }
}
