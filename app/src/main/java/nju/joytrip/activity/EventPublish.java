package nju.joytrip.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import nju.joytrip.R;
import nju.joytrip.datepicker.CustomDatePicker;
import nju.joytrip.datepicker.DateFormatUtils;
import nju.joytrip.entity.Event;
import nju.joytrip.entity.User;

public class EventPublish extends AppCompatActivity implements View.OnClickListener {

    private TextView selectedTimeTv;
    private CustomDatePicker timePicker;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("发布活动");
        setContentView(R.layout.activity_event_publish);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        submitBtn = (Button)findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(this);
        findViewById(R.id.ll_time).setOnClickListener(this);
        selectedTimeTv = (TextView)findViewById(R.id.select_time);
        initTimePicker();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.submit_btn:
                EditText title_text = (EditText) findViewById(R.id.titlt_text);
                EditText content_text = (EditText) findViewById(R.id.content_text);
                String title = title_text.getText().toString();
                String content = content_text.getText().toString();
                Event event = new Event();
                User user = BmobUser.getCurrentUser(User.class);
                event.setTitle(title);
                event.setContent(content);
                event.setUser(user);
                event.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EventPublish.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.ll_time:
                timePicker.show(selectedTimeTv.getText().toString());
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        timePicker.onDestroy();
    }

    private void initTimePicker(){
        String beginTime = "2000-01-01 00:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        selectedTimeTv.setText(endTime);

        timePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                selectedTimeTv.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        timePicker.setCancelable(true);
        timePicker.setCanShowPreciseTime(true);
        timePicker.setScrollLoop(true);
        timePicker.setCanShowAnim(true);
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
