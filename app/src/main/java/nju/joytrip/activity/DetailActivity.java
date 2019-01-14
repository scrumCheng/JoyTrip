package nju.joytrip.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;
import nju.joytrip.entity.Event;
import nju.joytrip.entity.Notice;
import nju.joytrip.entity.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button joinBtn;
    private TextView showDetailTv;
    private SimpleAdapter adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("活动详情");
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        joinBtn = (Button)findViewById(R.id.join_btn);
        joinBtn.setOnClickListener(this);
        showDetailTv = (TextView)findViewById(R.id.detail_btn);
        showDetailTv.setOnClickListener(this);
        String id = getIntent().getStringExtra("id");
        BmobQuery<Event> query = new BmobQuery<Event>();
        query.include("user");
        query.getObject(id, new QueryListener<Event>() {
            @Override
            public void done(Event event, BmobException e) {
                if (e == null){
                    String title = event.getTitle();
                    String content = event.getContent();
                    String beginTime = event.getBeginTime();
                    String createdTime = event.getCreatedAt();
                    ArrayList<String> arrayList = event.getUsers();
                    String joinedNum = String.valueOf(arrayList.size());
                    User user = event.getUser();
                    String username = user.getUsername();
                    String nickname = user.getNickname();
                    String userPic = user.getUserPic();
                    TextView titleTv = (TextView)findViewById(R.id.detail_title);
                    titleTv.setText(title);
                    TextView contentTv = (TextView)findViewById(R.id.detail_content);
                    contentTv.setText(content);
                    TextView createdTimeTv = (TextView)findViewById(R.id.detail_createTime);
                    createdTimeTv.setText(createdTime);
                    TextView beginTimeTv = (TextView)findViewById(R.id.detail_beginTime);
                    beginTimeTv.setText(beginTime);
                    TextView usersNumTv = (TextView)findViewById(R.id.detail_num);
                    usersNumTv.setText(joinedNum);
                    TextView userTv = (TextView)findViewById(R.id.detail_name);
                    if (nickname == null){
                        userTv.setText(username);
                    }else {
                        userTv.setText(nickname);
                    }
                    ImageView iv = (ImageView)findViewById(R.id.detail_pic);
                    Glide.with(DetailActivity.this)
                            .asBitmap()
                            .load(userPic)
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into(iv);
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        final String id = getIntent().getStringExtra("id");
        BmobQuery<Event> query = new BmobQuery<Event>();
        query.include("user");
        switch (v.getId()){
            case R.id.join_btn:
                final User user = BmobUser.getCurrentUser(User.class);
                query.getObject(id, new QueryListener<Event>() {
                    @Override
                    public void done(final Event event, BmobException e) {
                        if (e == null){
                            ArrayList<String> arrayList = event.getUsers();
                            final User publisher = event.getUser();
                            if (!arrayList.contains(user.getObjectId())){
                                arrayList.add(user.getObjectId());
                                event.setUsers(arrayList);
                                event.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                                        if (e == null){
                                            builder.setMessage("加入成功")
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String updatedNum = String.valueOf(event.getUsers().size());
                                                            TextView usersNumTv = (TextView)findViewById(R.id.detail_num);
                                                            usersNumTv.setText(updatedNum);
                                                        }
                                                    })
                                                    .show();
                                            Notice notice = new Notice();
                                            notice.setUser(user);
                                            notice.setEvent(event);
                                            notice.setPublishId(publisher.getObjectId());
                                            notice.setIsRead(0);
                                            notice.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {

                                                }
                                            });
                                            ArrayList<String> eventList = user.getEvents();
                                            Log.i("aaaaaaaaa", String.valueOf(eventList.size()));
                                            if (eventList == null || !eventList.contains(id)){
                                                eventList.add(id);
                                                user.setEvents(eventList);
                                                user.update(new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {

                                                    }
                                                });
                                            }

                                        }
                                    }
                                });

                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                                builder.setMessage("您已加入该活动")
                                        .setPositiveButton("确定", null)
                                        .show();
                            }
                        }
                    }
                });
                break;
            case R.id.detail_btn:
               query.getObject(id, new QueryListener<Event>() {
                   @Override
                   public void done(Event event, BmobException e) {
                       ArrayList<String> arrayList = event.getUsers();
                       final List<String> list = new ArrayList<>();
                       for(String userid : arrayList){
                           final HashMap hashMap = new HashMap();
                           BmobQuery<User> userQuery = new BmobQuery<User>();
                           userQuery.getObject(userid, new QueryListener<User>() {
                               @Override
                               public void done(User user, BmobException e) {
                                   String username = user.getUsername();
                                   //hashMap.put("username", username);
                                   list.add(username);

                               }
                           });
                       }
                       final Handler handler = new Handler();
                       handler.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               //Log.i("maplist", String.valueOf(list));
                               AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                               builder.setTitle("活动成员")
                                       .setItems(ListToString(list), null)
                                       .setPositiveButton("确定", null)
                                       .show();

                           }
                       }, 1000);
                   }
               });
        }
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

    public String[] ListToString(List<String> list){
        String[] str = new String[list.size()];
        for (int i = 0;i < list.size();i++){
            str[i] = list.get(i);
        }
        return str;
    }

}
