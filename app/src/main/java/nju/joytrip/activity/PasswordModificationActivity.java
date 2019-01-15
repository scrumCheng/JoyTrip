package nju.joytrip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import nju.joytrip.R;
import nju.joytrip.entity.User;

public class PasswordModificationActivity extends AppCompatActivity {

    private EditText editText_oldPassword, editText_newPassword, editText_confirmPassword;
    private Button button_confirmPasswordModification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_modification);

        setTitle("修改密码");
        Bmob.initialize(this, "f6fbdb11a6a945a3382bf9225de95646");

        editText_oldPassword = findViewById(R.id.editText_oldPassword);
        editText_newPassword = findViewById(R.id.editText_newPassword);
        editText_confirmPassword = findViewById(R.id.editText_confirmPassword);
        button_confirmPasswordModification = findViewById(R.id.btn_confirm_psd_modification);

        button_confirmPasswordModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = editText_oldPassword.getText().toString();
                String newPassword = editText_newPassword.getText().toString();
                String confirmPassword = editText_confirmPassword.getText().toString();
                if (oldPassword.isEmpty()){
                    Toast.makeText(PasswordModificationActivity.this, "请输入原密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.isEmpty()){
                    Toast.makeText(PasswordModificationActivity.this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(confirmPassword)){
                    Toast.makeText(PasswordModificationActivity.this, "新密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }
                final User user = new User();
                BmobUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toast.makeText(PasswordModificationActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PasswordModificationActivity.this, "密码修改失败！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("密码修改失败", user.getUsername() + " " + e.getMessage());
                        }
                    }
                });

            }
        });
    }
}
