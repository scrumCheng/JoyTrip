package nju.joytrip.entity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private String nickname;

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String s){
        this.nickname = s;
    }
}
