package nju.joytrip.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;
import nju.joytrip.entity.PWShare;
import nju.joytrip.entity.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ShareDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);
        setTitle("分享详情");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String id = getIntent().getStringExtra("id");
        BmobQuery<PWShare> query = new BmobQuery<PWShare>();
        query.include("user");
        query.addQueryKeys("user,content,username,userPic,nickName");
        query.getObject(id, new QueryListener<PWShare>() {
            @Override
            public void done(PWShare event, BmobException e) {
                if (e == null){
                    String content = event.getContent();
                    String createdTime = event.getCreatedAt();
                    User user = event.getUser();
                    String username = user.getUsername();
                    String nickname = user.getNickname();
                    String userPic = user.getUserPic();
                    TextView contentTv = (TextView)findViewById(R.id.share_detail_content);
                    contentTv.setText(content);
                    TextView createdTimeTv = (TextView)findViewById(R.id.share_detail_createTime);
                    createdTimeTv.setText(createdTime);
                    TextView userTv = (TextView)findViewById(R.id.share_detail_name);
                    if (nickname == null){
                        userTv.setText(username);
                    }else {
                        userTv.setText(nickname);
                    }
                    ImageView iv = (ImageView)findViewById(R.id.share_detail_pic);
                    Glide.with(ShareDetail.this)
                            .asBitmap()
                            .load(userPic)
                            .apply(bitmapTransform(new CropCircleTransformation()))
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
