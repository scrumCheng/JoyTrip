package nju.joytrip.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import java.util.zip.Inflater;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import nju.joytrip.R;

import nju.joytrip.activity.ShareDetail;
import nju.joytrip.entity.PWShare;


import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class UpdatesFragment extends Fragment {
    private SimpleAdapter adapter;
    private Button pubBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){

        View view = inflater.inflate(R.layout.fragment_updates, container, false);

        setHasOptionsMenu(true);

        ListView lv = (ListView)view.findViewById(R.id.share_item_list);
        loadData(lv);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.share_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.word:
                Intent intent1 = new Intent(getActivity(), nju.joytrip.activity.PicWordShare.class );
                startActivity(intent1);
                return true;
            case R.id.pic:
                Intent intent2 = new Intent(getActivity(), nju.joytrip.activity.PicWordShare.class);
                startActivity(intent2);
                return true;
            case R.id.video:
                Intent intent3 = new Intent(getActivity(), nju.joytrip.activity.PicWordShare.class);
                startActivity(intent3);
                return true;
        }
        return false;
    }

    private void loadData(final ListView lv){
        BmobQuery<PWShare> bmobQuery = new BmobQuery<PWShare>();
        bmobQuery.include("user");
        bmobQuery.addQueryKeys("user,content,username,userPic,nickName");
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<PWShare>() {
            @Override
            public void done(List<PWShare> list, BmobException e) {
                if (e == null){
                    List<Map<String, String>> mapList = new ArrayList<>();
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
                    adapter = new SimpleAdapter(getActivity(), mapList, R.layout.word_share_item,
                            new String[]{"userPic", "username",  "content", "time"},
                            new int[]{R.id.share_item_pic, R.id.share_item_name,  R.id.share_item_content, R.id.share_item_createTime});
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
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> h = (HashMap<String, String>)parent.getItemAtPosition(position);
                            String eventId = h.get("id");
                            Intent intent = new Intent(getActivity(), ShareDetail.class);
                            intent.putExtra("id", eventId);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
