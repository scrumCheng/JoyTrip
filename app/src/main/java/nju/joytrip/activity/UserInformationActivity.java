package nju.joytrip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import nju.joytrip.R;
import nju.joytrip.entity.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class UserInformationActivity extends AppCompatActivity {

    private ImageView imageView_portrait;
    private RelativeLayout layout_portrait, layout_nickname, layout_userid,
            layout_phonenumber, layout_gender, layout_email, layout_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        //初始化Bmob
        Bmob.initialize(this, "f6fbdb11a6a945a3382bf9225de95646");
        User user = BmobUser.getCurrentUser(User.class);

        layout_portrait = findViewById(R.id.layout_portrait);
        imageView_portrait = findViewById(R.id.information_activity_portrait);
        Glide.with(this)
                .load(user.getUserPic())
                .into(imageView_portrait);

        layout_nickname = findViewById(R.id.layout_nickname);
        ((TextView) layout_nickname.getChildAt(1)).setText(user.getNickname());

        layout_userid = findViewById(R.id.layout_userid);
        ((TextView) layout_userid.getChildAt(1)).setText(user.getUsername());

        layout_phonenumber = findViewById(R.id.layout_phonenumber);
        ((TextView) layout_phonenumber.getChildAt(1)).setText(user.getMobilePhoneNumber());

        layout_gender = findViewById(R.id.layout_gender);
        ((TextView) layout_gender.getChildAt(1)).setText(user.getGender());

        layout_email = findViewById(R.id.layout_email);
        ((TextView) layout_email.getChildAt(1)).setText(user.getEmail());
    }
}
