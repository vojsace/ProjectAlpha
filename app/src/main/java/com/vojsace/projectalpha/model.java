package com.vojsace.projectalpha;

public class model {
    private String msg;
    private String name;
    private String user_color;
    private String msg_id;

    public model() {
    }

    public model(String user_color, String msg, String name, String msg_id) {
        this.user_color = user_color;
        this.msg = msg;
        this.name = name;
        this.msg_id = msg_id;
    }

    public String getUser_color() {
        return user_color;
    }

    public void setUser_color(String user_color) {
        this.user_color = user_color;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }
}
