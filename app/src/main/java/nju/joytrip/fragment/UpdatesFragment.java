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
    private SimpleAdapter adapter;
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
                HashMap<String, String> h = (HashMap<String, String>)parent.getItemAtPosition(position);
                String eventId = h.get("id");
                Log.i("1231545" ,"45345345");
                Intent intent = new Intent(getActivity(), ShareDetail.class);
                intent.putExtra("id", eventId);
                startActivity(intent);
            }
        });
       init();
//        loadData();
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
                intent = new Intent(getActivity(), PicWordShare.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private void loadData(){
        BmobQuery<PWShare> bmobQuery = new BmobQuery<PWShare>();
        bmobQuery.include("user");
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<PWShare>() {
            @Override
            public void done(List<PWShare> list, BmobException e) {
                if (e == null){
                    final List<Map<String, String>> mapList = new ArrayList<>();
                    for (PWShare event : list){
                        String userPic = event.getUser().getUserPic();
                        String username = event.getUser().getUsername();
                        String nickname = event.getUser().getNickname();
                        if (nickname == null){
                                      nickname = username;
                        }
                        final HashMap mHashMap = new HashMap<>();
                        mHashMap.put("username",nickname);
                        Glide.with(UpdatesFragment.this)
                                .asBitmap()
                                .load(userPic)
                                .apply(bitmapTransform(new CropCircleTransformation()))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        mHashMap.put("userPic", resource);
                                    }
                                });
                        mHashMap.put("content", event.getContent());
                        mHashMap.put("time", event.getCreatedAt());
                        mHashMap.put("id", event.getObjectId());
                        mapList.add(mHashMap);

                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setListView(mapList);
                        }
                    }, 50);

                }
                else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });
    }
    private void setListView(List<Map<String, String>> mapList){
        adapter = new SimpleAdapter(getActivity(), mapList, R.layout.share_item,
                new String[]{"userPic", "username",  "content", "time"},
                new int[]{R.id.write_photo, R.id.write_name,  R.id.dynamic_text, R.id.write_date});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap){
                    ImageView iv = (ImageView)view;
                    iv.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }
        });
        lv.setAdapter(adapter);

    }



}
