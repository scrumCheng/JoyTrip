package nju.joytrip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import nju.joytrip.R;

public class FriendActivity extends AppCompatActivity {

    private ListView listView_myFriends;
    private List<Map<String, String>> mapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        setTitle("我的好友");
    }
}
