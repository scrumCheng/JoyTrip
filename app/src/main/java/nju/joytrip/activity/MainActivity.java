package nju.joytrip.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import nju.joytrip.R;
import nju.joytrip.entity.User;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import nju.joytrip.R;
import nju.joytrip.fragment.HomeFragment;
import nju.joytrip.fragment.MineFragment;
import nju.joytrip.fragment.UpdatesFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private HomeFragment homeFragment;
    private UpdatesFragment updatesFragment;
    private MineFragment mineFragment;
    private View homeLayout;
    private View updatesLayout;
    private View mineLayout;
    private ImageView homeImg;
    private ImageView updatesImg;
    private ImageView mineImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "f6fbdb11a6a945a3382bf9225de95646");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        User user = BmobUser.getCurrentUser(User.class);
        Log.i("username",user.getUsername());
        initViews();
        setTabSelection(0);
    }

    private void initViews(){
        homeLayout = (LinearLayout)findViewById(R.id.home_layout);
        homeImg = (ImageView)findViewById(R.id.home_img);
        homeLayout.setOnClickListener(this);
        updatesLayout = (LinearLayout)findViewById(R.id.updates_layout);
        updatesImg = (ImageView)findViewById(R.id.updates_img);
        updatesLayout.setOnClickListener(this);
        mineLayout = (LinearLayout)findViewById(R.id.mine_layout);
        mineImg = (ImageView)findViewById(R.id.mine_img);
        mineLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        resetIndicator();
        switch (v.getId()){
            case R.id.home_layout:
                setTabSelection(0);
                break;
            case R.id.updates_layout:
                setTabSelection(1);
                break;
            case R.id.mine_layout:
                setTabSelection(2);
                break;
        }
    }

    private void setTabSelection(int index){
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case 0:
                if (homeFragment == null){
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_content, homeFragment);
                }else {
                    transaction.show(homeFragment);
                }
                homeImg.setImageResource(R.mipmap.indicator_home_active);
                break;
            case 1:

                if (updatesFragment == null){
                    updatesFragment = new UpdatesFragment();
                    transaction.add(R.id.fragment_content, updatesFragment);
                }else {
                    transaction.show(updatesFragment);
                }
                updatesImg.setImageResource(R.mipmap.indicator_updates_active);
                break;
            case 2:
                if (mineFragment == null){
                    mineFragment = new MineFragment();
                    transaction.add(R.id.fragment_content, mineFragment);
                }else {
                    transaction.show(mineFragment);
                }
                mineImg.setImageResource(R.mipmap.indicator_mine_active);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (homeFragment != null){
            transaction.hide(homeFragment);
        }
        if (updatesFragment != null){
            transaction.hide(updatesFragment);
        }
        if (mineFragment != null){
            transaction.hide(mineFragment);
        }
    }

    private void resetIndicator(){
        homeImg.setImageResource(R.mipmap.indicator_home_inactive);
        updatesImg.setImageResource(R.mipmap.indicator_updates_inactive);
        mineImg.setImageResource(R.mipmap.indicator_mine_inactive);
    }

}
