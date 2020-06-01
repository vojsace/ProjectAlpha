package com.vojsace.projectalpha;

public class model {
    private String msg;
    private String name;
    private String user_color;

    public model() {
    }

    public model(String user_color, String msg, String name) {
        this.user_color = user_color;
        this.msg = msg;
        this.name = name;
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
}
