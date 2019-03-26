package nju.joytrip.entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class PWShare extends BmobObject {
    private String title;
    private String content;
    private User user;
    private ArrayList<String> photoList;

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

    public ArrayList<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<String> photoList) {
        this.photoList = photoList;
    }

}
