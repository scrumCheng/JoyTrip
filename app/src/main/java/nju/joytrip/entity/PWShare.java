package nju.joytrip.entity;

import cn.bmob.v3.BmobObject;

public class PWShare extends BmobObject {
    private String title;
    private String content;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


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
}
