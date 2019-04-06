package nju.joytrip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import nju.joytrip.R;
import nju.joytrip.entity.User;

public class FriendActivity extends AppCompatActivity {

    private ListView listView_myFriends;
    private List<Map<String, String>> mapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        listView_myFriends = findViewById(R.id.lv_myfriends);
        mapList = new ArrayList<>();

        final User user = BmobUser.getCurrentUser(User.class);
        if (user.getEvents() == null){
            return;
        }
    }
}
