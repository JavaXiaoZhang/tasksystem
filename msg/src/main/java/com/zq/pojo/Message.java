package com.zq.pojo;

import java.util.Map;

/**
 * @author ZQ
 * @Date 2020/3/29
 */
public class Message {
    private String msgType;
    private Map<String, Object> data;

    public Message() {
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Message(String msgType, Map<String, Object> data) {
        this.msgType = msgType;
        this.data = data;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }


}
