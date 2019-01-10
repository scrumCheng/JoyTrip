package nju.joytrip.entity;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class Event extends BmobObject {
    private String title;
    private String content;
    private String beginTime;
    private User user;
    private ArrayList<String> users;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
