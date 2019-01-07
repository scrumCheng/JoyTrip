package nju.joytrip.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import nju.joytrip.R;
import nju.joytrip.entity.Event;
import nju.joytrip.entity.User;
import nju.joytrip.utils.GlideCircleTransform;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("活动详情");
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String id = getIntent().getStringExtra("id");
        BmobQuery<Event> query = new BmobQuery<Event>();
        query.include("user");
        query.getObject(id, new QueryListener<Event>() {
            @Override
            public void done(Event event, BmobException e) {
                if (e == null){
                    String title = event.getTitle();
                    String content = event.getContent();
                    String createdTime = event.getCreatedAt();
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
                    TextView userTv = (TextView)findViewById(R.id.detail_name);
                    if (nickname == null){
                        userTv.setText(username);
                    }else {
                        userTv.setText(nickname);
                    }
                    ImageView iv = (ImageView)findViewById(R.id.detail_pic);
                    Glide.with(DetailActivity.this)
                            .load(userPic)
                            .transform(new GlideCircleTransform(DetailActivity.this))
                            .into(iv);
                }
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
