package nju.joytrip.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;

import nju.joytrip.activity.PicWordShare;
import nju.joytrip.activity.ShareDetail;
import nju.joytrip.adapter.ShareAdapter;
import nju.joytrip.entity.PWShare;


import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class UpdatesFragment extends Fragment {
    private ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){

        View view = inflater.inflate(R.layout.fragment_updates, container, false);
        setHasOptionsMenu(true);
        lv = (ListView)view.findViewById(R.id.share_item_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PWShare h = (PWShare) parent.getItemAtPosition(position);
                String eventId = h.getObjectId();
                Log.i("1231545" ,"45345345");
                Intent intent = new Intent(getActivity(), ShareDetail.class);
                intent.putExtra("id", eventId);
                startActivity(intent);
            }
        });
       init();
       return view;
    }

    public void init(){
        BmobQuery<PWShare> bmobQuery = new BmobQuery<PWShare>();
        bmobQuery.include("user");
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<PWShare>() {
            @Override
            public void done(List<PWShare> list, BmobException e) {
                if (e == null) {
                    lv.setAdapter(new ShareAdapter(getActivity(), R.layout.share_item, list));
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.share_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch(id){
            case R.id.menu_item_pw_share:
                intent = new Intent(getActivity(), PicWordShare.class );
                startActivity(intent);
                return true;
            case R.id.menu_item_v_share:
                Toast.makeText(getActivity(), "敬请期待", Toast.LENGTH_SHORT).show();
                return true;
//                intent = new Intent(getActivity(), PicWordShare.class);
//                startActivity(intent);
//                return true;
        }
        return false;
    }
}
