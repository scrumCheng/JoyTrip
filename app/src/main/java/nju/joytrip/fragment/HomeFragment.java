package nju.joytrip.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

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
import nju.joytrip.activity.DetailActivity;
import nju.joytrip.activity.EventPublish;
import nju.joytrip.entity.Event;
import nju.joytrip.entity.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class HomeFragment extends Fragment {
    private SimpleAdapter adapter;
    private Button pubBtn;
    private ListView lv;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lv = (ListView)view.findViewById(R.id.item_list);
        loadData();
        pubBtn = (Button)view.findViewById(R.id.publish_btn);
        pubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventPublish.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    private void loadData(){
        BmobQuery<Event> bmobQuery = new BmobQuery<Event>();
        bmobQuery.include("user");
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<Event>() {
            @Override
            public void done(List<Event> list, BmobException e) {
                if (e == null){
                    final List<Map<String, String>> mapList = new ArrayList<>();
                    for (Event event : list){
                        User user = event.getUser();
                        String userPic = user.getUserPic();
                        String username = user.getUsername();
                        String nickname = user.getNickname();
                        if (nickname == null){
                            nickname = username;
                        }
                        final HashMap mHashMap = new HashMap<>();
                        Glide.with(HomeFragment.this)
                                .asBitmap()
                                .load(userPic)
                                .apply(bitmapTransform(new CropCircleTransformation()))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        mHashMap.put("userPic", resource);
                                    }
                                });
                       mHashMap.put("username",nickname);
                       mHashMap.put("title", event.getTitle());
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
                    }, 500);
                }else {
                    Log.i("error", e.toString());
                }
            }
        });
    }

    private void setListView(List<Map<String, String>> mapList){
        adapter = new SimpleAdapter(getActivity(), mapList, R.layout.event_item,
                new String[]{"userPic", "username", "title", "content", "time"},
                new int[]{R.id.item_pic, R.id.item_name, R.id.item_title, R.id.item_content, R.id.item_createTime});
        adapter.setViewBinder(new ViewBinder() {
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
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id", eventId);
                startActivity(intent);
            }
        });

    }

}
