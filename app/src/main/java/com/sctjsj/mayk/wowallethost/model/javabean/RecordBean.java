package com.sctjsj.mayk.wowallethost.model.javabean;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/7/27.
 */

public class RecordBean implements Serializable {
    private String name;
    private String content;
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RecordBean{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
