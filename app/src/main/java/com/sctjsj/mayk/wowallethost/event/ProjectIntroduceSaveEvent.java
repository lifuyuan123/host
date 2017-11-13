package com.sctjsj.mayk.wowallethost.event;

/**
 * Created by mayikang on 17/5/16.
 */

public class ProjectIntroduceSaveEvent {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ProjectIntroduceSaveEvent(String content) {

        this.content = content;
    }
}
