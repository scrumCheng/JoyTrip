package nju.joytrip.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import nju.joytrip.R;
import nju.joytrip.entity.User;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        //初始化Bmob
        Bmob.initialize(getContext(), "f6fbdb11a6a945a3382bf9225de95646");
        User user = BmobUser.getCurrentUser(User.class);

        imageView_portraitBackgroud = view.findViewById(R.id.mine_portrait_background);
        imageView_portrait = view.findViewById(R.id.mine_portrait);
        textView_nickName = view.findViewById(R.id.mine_nickname);


        return view;
    }
}
