package nju.joytrip.activity;

import android.content.Intent;
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
import nju.joytrip.entity.User;

public class SignUpActivity extends AppCompatActivity {

    private Button button_sign;
    private EditText editText_username;
    private EditText editText_password;
    private EditText editText_confirmPWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //初始化Bmob
        Bmob.initialize(this, "f6fbdb11a6a945a3382bf9225de95646");

        //绑定控件
        button_sign = findViewById(R.id.button_sign);
        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);
        editText_confirmPWD = findViewById(R.id.editText_confirmPassword);

        //登录及注册事件监听
        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String t_name = editText_username.getText().toString();
                final String t_pwd = editText_password.getText().toString();
                String t_confirmPWD = editText_confirmPWD.getText().toString();
                if(t_name.equals(""))
                    Toast.makeText(SignUpActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
                else if(t_pwd.equals(t_confirmPWD)==false)
                    Toast.makeText(SignUpActivity.this,"两次密码不一致",Toast.LENGTH_LONG).show();
                else {
                    final User user = new User();
                    user.setUsername(t_name);
                    user.setPassword(t_pwd);
                    user.setUserPic("http://bmob-cdn-23312.b0.upaiyun.com/2019/01/06/5ef76d8d40343de7803494a7011d5f11.jpg");
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e==null){
                                Toast.makeText(SignUpActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                                Intent intent_backResult = new Intent();
                                intent_backResult.putExtra("signState",true);
                                intent_backResult.putExtra("username",t_name);
                                intent_backResult.putExtra("password",t_pwd);
                                SignUpActivity.this.setResult(1,intent_backResult);
                                SignUpActivity.this.finish();
                            }else {
                                Toast.makeText(SignUpActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
