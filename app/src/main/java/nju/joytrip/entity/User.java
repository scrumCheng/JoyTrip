package nju.joytrip.entity;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    private String nickName;
    private String userPic;
    private String localPic;
    private String gender;
    private ArrayList<String> events;

    public String getNickname(){
        return nickName;
    }

    public void setNickname(String s){
        this.nickName = s;
    }

    public String getUserPic(){
        return userPic;
    }

    public void setUserPic(String url){
        this.userPic = url;
    }

    /*
    public String getLocalPic(){
        return localPic;
    }

    public void setLocalPic(String path){
        this.localPic = path;
    }
    */

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }
}
