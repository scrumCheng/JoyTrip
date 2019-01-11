package nju.joytrip.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import nju.joytrip.entity.Notice;
import nju.joytrip.entity.User;
import nju.joytrip.fragment.HomeFragment;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class NoticeActivity extends AppCompatActivity {
    private SimpleAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("通知");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_notice);
        listView = (ListView)findViewById(R.id.notice_list);
        loadData();
    }

    private void loadData(){
        BmobQuery<Notice> query = new BmobQuery<Notice>();
        User me = BmobUser.getCurrentUser(User.class);
        String meId = me.getObjectId();
        query.include("user, event");
        query.addWhereEqualTo("publishId", meId);
        query.order("-createdAt");
        query.findObjects(new FindListener<Notice>() {
            @Override
            public void done(List<Notice> list, BmobException e) {
                final List<Map<String, String>> mapList = new ArrayList<>();
                for (Notice notice : list){
                    Event event = notice.getEvent();
                    User user = notice.getUser();
                    String userPic = user.getUserPic();
                    String username = user.getUsername();
                    String nickname = user.getNickname();
                    if (nickname == null){
                        nickname = username;
                    }
                    final HashMap mHashMap = new HashMap<>();
                    Glide.with(NoticeActivity.this)
                            .asBitmap()
                            .load(userPic)
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    mHashMap.put("userPic", resource);
                                }
                            });
                    mHashMap.put("username",nickname);
                    mHashMap.put("time", notice.getCreatedAt());
                    mHashMap.put("id", event.getObjectId());
                    mapList.add(mHashMap);
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setListView(mapList);
                    }
                }, 500);
            }
        });
    }

    private void setListView(List<Map<String, String>> mapList){
        adapter = new SimpleAdapter(NoticeActivity.this, mapList, R.layout.notice_item,
                new String[]{"userPic", "username", "time"},
                new int[]{R.id.notice_pic, R.id.notice_name, R.id.notice_time});
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
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> h = (HashMap<String, String>)parent.getItemAtPosition(position);
                String eventId = h.get("id");
                Intent intent = new Intent(NoticeActivity.this, DetailActivity.class);
                intent.putExtra("id", eventId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
