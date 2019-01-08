package nju.joytrip.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import nju.joytrip.R;
import nju.joytrip.entity.E_WordShare;
import nju.joytrip.entity.Event;

public class WShareDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);
        setTitle("分享详情");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String id = getIntent().getStringExtra("id");
        BmobQuery<E_WordShare> query = new BmobQuery<E_WordShare>();
        query.getObject(id, new QueryListener<E_WordShare>() {
            @Override
            public void done(E_WordShare event, BmobException e) {
                if (e == null){
                    String title = event.getTitle();
                    String content = event.getContent();
                    TextView titleTv = (TextView)findViewById(R.id.share_detail_title);
                    titleTv.setText(title);
                    TextView contentTv = (TextView)findViewById(R.id.share_detail_content);
                    contentTv.setText(content);
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
