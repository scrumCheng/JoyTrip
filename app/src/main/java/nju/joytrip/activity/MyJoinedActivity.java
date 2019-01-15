package nju.joytrip.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;
import nju.joytrip.entity.Event;
import nju.joytrip.entity.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MyJoinedActivity extends AppCompatActivity {

    private ListView listView_myJoined;
    private List<Map<String, String>> mapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_joined);

        setTitle("我的加入 ");
        listView_myJoined = findViewById(R.id.lv_myJoined);
        mapList = new ArrayList<>();

        final User user = BmobUser.getCurrentUser(User.class);
        if (user.getEvents() == null){
            return;
        }
        BmobQuery<Event> query = new BmobQuery<Event>();
        query.include("objectId");
        query.addWhereContainedIn("objectId", user.getEvents());
        query.findObjects(new FindListener<Event>() {
            @Override
            public void done(List<Event> list, BmobException e) {
                if (e == null){
                    for (Event event : list) {
                        final HashMap mHashMap = new HashMap();
                        Glide.with(MyJoinedActivity.this)
                                .asBitmap()
                                .load(user.getUserPic())
                                .apply(bitmapTransform(new CropCircleTransformation()))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        mHashMap.put("userPic", resource);
                                    }
                                });
                        mHashMap.put("username",user.getNickname());
                        mHashMap.put("title", event.getTitle());
                        mHashMap.put("content", event.getContent());
                        mHashMap.put("time", event.getCreatedAt());
                        mHashMap.put("id", event.getObjectId());
                        mapList.add(mHashMap);
                    }
                    loadData();
                } else {
                    Toast.makeText(MyJoinedActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    Log.d("我的加入查询失败", e.toString());
                }
            }
        });
    }

    private void loadData() {
        SimpleAdapter adapter = new SimpleAdapter(MyJoinedActivity.this, mapList, R.layout.event_item,
                new String[]{"userPic", "username", "title", "content", "time"},
                new int[]{R.id.item_pic, R.id.item_name, R.id.item_title, R.id.item_content, R.id.item_createTime});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap){
                    ImageView iv = (ImageView)view;
                    iv.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        listView_myJoined.setAdapter(adapter);
        listView_myJoined.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> h = (HashMap<String, String>)parent.getItemAtPosition(position);
                String eventId = h.get("id");
                Intent intent = new Intent(MyJoinedActivity.this, DetailActivity.class);
                intent.putExtra("id", eventId);
                startActivity(intent);
            }
        });
    }
}
