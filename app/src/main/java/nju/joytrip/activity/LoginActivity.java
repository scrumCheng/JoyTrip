package nju.joytrip.activity;

import android.content.Intent;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import nju.joytrip.R;
import nju.joytrip.entity.User;

public class LoginActivity extends AppCompatActivity {

    private Button button_signUp;
    private Button button_login;
    private EditText editText_username;
    private EditText editText_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化Bmob
        Bmob.initialize(this, "f6fbdb11a6a945a3382bf9225de95646");
        //Bmob.initialize(this, "5e67ccbfcfd54f76519eb3482ca95239");
        //绑定控件
        button_login = (Button) findViewById(R.id.button_login);
        button_signUp = (Button) findViewById(R.id.button_signUp);
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);

        //登陆状态判断-已经登陆则直接跳转到MainActivity
        if(BmobUser.isLogin()){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            this.startActivity(intent);
            this.finish();
        }

        //监听登录按钮
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = new User();
                String t_name = editText_username.getText().toString();
                String t_password = editText_password.getText().toString();
                if (t_name.equals("") || t_password.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入完整信息",Toast.LENGTH_LONG).show();
                } else {
                    user.setUsername(t_name);
                    user.setPassword(t_password);
                    user.login(new SaveListener<User>() {
                        public void done(User bmobUser, BmobException e){
                            if (e == null){
                                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                                BmobFile default_user = new BmobFile("default_user.jpg","","http://bmob-cdn-23312.b0.upaiyun.com/2019/01/06/5ef76d8d40343de7803494a7011d5f11.jpg");
                                default_user.download(new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e == null){
                                            Log.i("LocalPic路径",s);
                                        }else {
                                            Toast.makeText(LoginActivity.this,"下载头像失败",Toast.LENGTH_LONG);
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }
                                });
                            }else {
                                Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        //监听注册按钮
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            boolean signState = data.getExtras().getBoolean("signState");
            if (signState == true) {
                String t_name = data.getExtras().getString("username");
                String t_password = data.getExtras().getString("password");
                BmobUser.loginByAccount(t_name, t_password, new LogInListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
}
