package com.cstdr.chatgpt.bean;

public class ChatMessage {

    private String msg;

    private String owner;

    public ChatMessage(String owner, String msg) {
        this.owner = owner;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "msg='" + msg + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
