package com.sctjsj.mayk.wowallethost.event;

import com.sctjsj.mayk.wowallethost.model.javabean.MessageBean;

/**
 * Created by lifuy on 2017/7/9.
 */

public class MessageEvent {
    private MessageBean messageBean;

    public MessageEvent(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    public MessageEvent() {
    }

    public MessageBean getMessageBean() {
        return messageBean;
    }
}
