package nju.joytrip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import nju.joytrip.R;
import nju.joytrip.entity.Event;

public class EventPublish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "f6fbdb11a6a945a3382bf9225de95646");
        setContentView(R.layout.activity_event_publish);
        Button submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title_text = (EditText) findViewById(R.id.titlt_text);
                EditText content_text = (EditText) findViewById(R.id.content_text);
                String title = title_text.getText().toString();
                String content = content_text.getText().toString();
                Event event = new Event();
                event.setTitle(title);
                event.setContent(content);
                event.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
