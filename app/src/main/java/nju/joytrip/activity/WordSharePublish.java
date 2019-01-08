package nju.joytrip.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import nju.joytrip.R;
import nju.joytrip.entity.E_WordShare;

public class WordSharePublish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_share_publish);
        setTitle("发表分享");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button submit_btn = (Button) findViewById(R.id.share_submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title_text = (EditText) findViewById(R.id.share_title_text);
                EditText content_text = (EditText) findViewById(R.id.share_content_text);
                String title = title_text.getText().toString();
                String content = content_text.getText().toString();
                E_WordShare share = new E_WordShare();
                share.setTitle(title);
                share.setContent(content);
                share.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(WordSharePublish.this, CheckWordShare.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
