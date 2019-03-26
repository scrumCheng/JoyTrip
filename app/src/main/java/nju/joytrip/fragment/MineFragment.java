package nju.joytrip.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;
import nju.joytrip.activity.EventPublish;
import nju.joytrip.activity.MyJoinedActivity;
import nju.joytrip.activity.MyPublishedActivity;
import nju.joytrip.activity.NoticeActivity;
import nju.joytrip.activity.UserInformationActivity;
import nju.joytrip.entity.Notice;
import nju.joytrip.entity.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * <p>Title: MineFragment</p>
 * <p>Description: MineFragment（singleTask）</p>
 * @author 程曦
 * @version 1.0
 */
public class MineFragment extends Fragment {
    private ImageView imageView_portraitBackgroud;
    private ImageView imageView_portrait;
    private TextView textView_nickName;
    private RelativeLayout myPublished, myFollowed, myNotification;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        //初始化Bmob
        Bmob.initialize(getContext(), "f6fbdb11a6a945a3382bf9225de95646");
        user = BmobUser.getCurrentUser(User.class);

        imageView_portraitBackgroud = view.findViewById(R.id.mine_portrait_background);
        imageView_portrait = view.findViewById(R.id.mine_portrait);
        textView_nickName = view.findViewById(R.id.mine_nickname);
        myPublished = view.findViewById(R.id.mine_mypublished);
        myFollowed = view.findViewById(R.id.mine_myfollowed);
        myNotification = view.findViewById(R.id.mine_mynotification);

        loadUserInfo();

        Glide.with(this)
                .load(R.mipmap.background)
                .apply(bitmapTransform(new BlurTransformation(3, 4)))
                .into(imageView_portraitBackgroud);

        imageView_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInformationActivity.class);
                startActivity(intent);
            }
        });

        myPublished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPublishedActivity.class);
                getActivity().startActivity(intent);
            }
        });

        myFollowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyJoinedActivity.class);
                getActivity().startActivity(intent);
            }
        });

        myNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void loadUserInfo(){
        Glide.with(this)
                .load(user.getUserPic())
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(imageView_portrait);

        if (user.getNickname() != null) {
            textView_nickName.setText(user.getNickname());
        } else {
            textView_nickName.setText(user.getUsername());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){

        }else {
           loadUserInfo();
        }
    }

}
