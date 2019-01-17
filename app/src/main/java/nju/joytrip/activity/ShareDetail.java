package nju.joytrip.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liji.imagezoom.util.ImageZoom;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;
import nju.joytrip.adapter.GridAdapter;
import nju.joytrip.customview.MyGridView;
import nju.joytrip.entity.PWShare;
import nju.joytrip.entity.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ShareDetail extends AppCompatActivity {
    private TextView mcontentTv;
    private TextView mcreatedTimeTv;
    private TextView muserTv;
    private ImageView miv;
    private MyGridView mgridView;

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
                    mcontentTv = (TextView)findViewById(R.id.share_detail_content);
                    mcontentTv.setText(content);
                    mcreatedTimeTv = (TextView)findViewById(R.id.share_detail_createTime);
                    mcreatedTimeTv.setText(createdTime);
                    muserTv = (TextView)findViewById(R.id.share_detail_name);

                    if (nickname == null){
                        muserTv.setText(username);
                    }else {
                        muserTv.setText(nickname);
                    }
                    miv = (ImageView)findViewById(R.id.share_detail_user_pic);
                    Glide.with(ShareDetail.this)
                            .asBitmap()
                            .load(userPic)
                            .apply(bitmapTransform(new CropCircleTransformation()))
                            .into(miv);
                    mgridView = (MyGridView)findViewById(R.id.share_dynamic_photo);
                    final List<String> l = new ArrayList<>();
                    if(event.getPhotoList().size()!=0){
                        l.addAll(event.getPhotoList());
                    }
                    mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String img = (String)parent.getItemAtPosition(position);
                            ImageZoom.show(ShareDetail.this,img,l);
                        }
                    });
                    mgridView.setNumColumns(3);
                    GridAdapter mgridAdapter = new GridAdapter(ShareDetail.this,event.getPhotoList());
                    mgridView.setAdapter(mgridAdapter);
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
