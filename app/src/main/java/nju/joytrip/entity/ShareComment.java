package nju.joytrip.entity;

import cn.bmob.v3.BmobObject;

public class ShareComment extends BmobObject {
    private String content;
    private PWShare event;
    private User user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PWShare getEvent() {
        return event;
    }

    public void setEvent(PWShare event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
